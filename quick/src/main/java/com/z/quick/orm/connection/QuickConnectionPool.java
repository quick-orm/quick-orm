package com.z.quick.orm.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.monitor.MonitorSql;

public class QuickConnectionPool {
	private static final Log log = LogFactory.get();
	private JDBCConfig jdbcConfig;
	private final ReentrantLock lock = new ReentrantLock(true);
	private final List<Connection> unUsedConn = new LinkedList<Connection>();
	private final List<Connection> usedConn = new LinkedList<Connection>();
	private volatile AtomicInteger connSize = new AtomicInteger(0);
	private volatile ThreadLocal<Long> waitTime = new ThreadLocal<>();
	/**用于处理空闲、过期连接*/
	private final ScheduledExecutorService clearService = Executors.newSingleThreadScheduledExecutor();
	
	public QuickConnectionPool(JDBCConfig jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
		initConnectionPool(jdbcConfig.getInitialPoolSize());
		if (jdbcConfig.getExecuteTimeMonitor()) {//开启sql执行耗时监控
			MonitorSql.start();
		}
	}
	/**
	 * ********************************************
	 * method name   : initConnectionPool 
	 * description   : 初始化连接
	 * @return       : void
	 * @param        : @param initialSize
	 * modified      : zhukaipeng ,  2017年8月18日  下午11:45:31
	 * @see          : 
	 * *******************************************
	 */
	public void initConnectionPool(int initialSize){
		lock.lock();
		try {
			for (int i = 0; i < initialSize; i++) {
				unUsedConn.add(createConnection());
			}
			timerClearAllLeisureConnection();
			log.info("===============Initialize DB Connection Pool SUCCESS===============");
		} catch (Exception e) {
			log.error("初始化数据库连接出现异常",e);
			throw new RuntimeException(e);
		}finally {
			lock.unlock();
		}
	}
	/**
	 * ********************************************
	 * method name   : createConnection 
	 * description   : 创建新连接
	 * @return       : Connection
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年8月18日  下午11:45:45
	 * @see          : 
	 * *******************************************
	 */
	private Connection createConnection(){
		lock.lock();
		try {
			Connection conn = DriverManager.getConnection(jdbcConfig.getUrl(),
					jdbcConfig.getUsername(), jdbcConfig.getPassword());
			connSize.incrementAndGet();
			Connection qcw = new QuickConnectionWrapper(conn,this);
			log.debug("创建连接：{}成功",qcw);
			return qcw;
		} catch (SQLException e) {
			log.error("创建数据库连接出错",e);
			throw new RuntimeException("创建数据库连接出错",e);
		}finally{
			lock.unlock();
		}
	}
	/**
	 * ********************************************
	 * method name   : setConnectionLastUserdTime 
	 * description   : 设置连接最后使用时间
	 * @return       : void
	 * @param        : @param conn
	 * modified      : zhukaipeng ,  2017年8月17日  上午11:03:08
	 * @see          : 
	 * *******************************************
	 */
	private void setConnectionLastUserdTime(Connection conn){
		QuickConnectionWrapper qcw = (QuickConnectionWrapper) conn;
		qcw.setLastUsedTime(System.currentTimeMillis());
	}
	public Connection getConnection(){
		lock.lock();
		boolean isReleaseLock= true;
		try {
			if (unUsedConn.size() > 0) {
				Connection conn = unUsedConn.get(0);
				setConnectionLastUserdTime(conn);
				unUsedConn.remove(conn);
				usedConn.add(conn);
				return conn;
			}
			if (connSize.get() < jdbcConfig.getMaxPoolSize()) {
				Connection conn = createConnection();
				usedConn.add(conn);
				return conn;
			}
			isReleaseLock = false;
			lock.unlock(); //等待连接期间释放锁
			while(true){
				if (waitTime.get() == null) {
					waitTime.set(0L);
				}
				if (waitTime.get() > jdbcConfig.getMaxWait()) {
					waitTime.set(0L);
					throw new RuntimeException("当前数据库连接已达上线，无法再创建连接");
				}
				waitTime.set(waitTime.get()+2000);
				wait(2000);
				return getConnection();
			}
			
		} catch (Exception e) {
			log.error("获取数据库连接出错",e);
			throw new RuntimeException("获取数据库连接出错",e);
		}finally {
			if (isReleaseLock) {
				lock.unlock();
			}
		}
	}
	public Connection getConnection(String username, String password) throws SQLException {
		if (jdbcConfig.getUsername().equals(username) && jdbcConfig.getPassword().equals(password)) {
			return getConnection();
		}
		throw new RuntimeException("用户名密码错误!");
	}
	private void wait(int mSeconds) {  
        try {  
            Thread.sleep(mSeconds);  
        } catch (InterruptedException e) {  
        }  
    }  
	/**
	 * ********************************************
	 * method name   : timerClearAllLeisureConnection 
	 * description   : 定时释放空闲链接、定时清除已使用但长时间未释放的连接
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年8月17日  下午4:30:21
	 * @see          : 
	 * *******************************************
	 */
	public void timerClearAllLeisureConnection(){
		timerClearUnUsedConnection();
		timerClearUsedConnection();
	}
	/**
	 * ********************************************
	 * method name   : timerClearUnUsedConnection 
	 * description   : 空闲连接清理
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年8月17日  上午10:41:28
	 * @see          : 
	 * *******************************************
	 */
	public void timerClearUnUsedConnection(){
		if (jdbcConfig.getIdleConnectionTestPeriod() <= 0) {
			return;
		}
		clearService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					log.debug("开始清理空闲连接。【已创建连接：{}，已使用：{}，空闲：{}】",connSize.get(),usedConn.size(),unUsedConn.size());
					if (unUsedConn.size() <= jdbcConfig.getMinPoolSize()) 
						return;
					unUsedConn.removeIf(c -> {
						if (unUsedConn.size() <= jdbcConfig.getMinPoolSize()) 
							return false;
						QuickConnectionWrapper qcw = (QuickConnectionWrapper) c;
						if (getIdleTime(qcw.getLastUsedTime()) < jdbcConfig.getMaxIdleTime()) 
							return false;
						try {
							qcw.destroy();
						} catch (Exception e) {
							log.error("关闭连接出现异常", e);
						}
						connSize.decrementAndGet();
						log.debug("销毁连接：{}成功",qcw);
						return true;
						
					});
				} catch (Exception e) {
					log.error(e,"清理空闲连接出现异常");
				}finally{
					lock.unlock();
				}
			}
		}, jdbcConfig.getIdleConnectionTestPeriod(), jdbcConfig.getIdleConnectionTestPeriod(), TimeUnit.MILLISECONDS);
		
	}
	/**
	 * ********************************************
	 * method name   : timerClearUsedConnection 
	 * description   : 清理已使用但未归还连接，每五分钟执行一次，若获取连接后，过了15分钟还未归还，则关闭此连接。
	 *                 规避连接泄露风险
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年8月17日  上午10:41:00
	 * @see          : 
	 * *******************************************
	 */
	public void timerClearUsedConnection(){
		int interval = 5*60*1000;
		int idle = 15*60*1000;
				
		clearService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					log.info("开始清理已使用未归还连接。【已创建连接：{}，已使用：{}，空闲：{}】",connSize.get(),usedConn.size(),unUsedConn.size());
					usedConn.removeIf(c -> {
						QuickConnectionWrapper qcw = (QuickConnectionWrapper) c;
						if (getIdleTime(qcw.getLastUsedTime()) < idle) {
							return false;
						}
						try {
							qcw.destroy();
						} catch (Exception e) {
							log.error("关闭连接出现异常", e);
						}
						connSize.decrementAndGet();
						log.debug("销毁连接：{}成功", qcw);
						return true;
					}); 
				} catch (Exception e) {
					log.error(e,"清理已使用连接出现异常");
				}finally{
					lock.unlock();
				}
			}
		}, interval, interval, TimeUnit.MILLISECONDS);
		
	}
	
	private long getIdleTime(long lastUsedTime){
		return System.currentTimeMillis() - lastUsedTime;
	}
	
	public void recycleConnection(Connection conn){
		lock.lock();
		try {
			usedConn.remove(conn);
			unUsedConn.add(conn);
//			log.debug("回收连接：{}成功",conn);
//			log.debug("当前连接情况【已创建：{}，已使用：{}，空闲：{}】",connSize.get(),usedConn.size(),unUsedConn.size());
		} catch (Exception e) {
			log.error("回收连接出现异常", e);
			throw new RuntimeException("回收连接出现异常",e);
		}finally{
			lock.unlock();
		}
	}
}
