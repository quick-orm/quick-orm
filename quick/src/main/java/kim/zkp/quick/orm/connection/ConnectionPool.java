/**
 * Copyright (c) 2017, ZhuKaipeng 朱开鹏 (2076528290@qq.com).

 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.zkp.quick.orm.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import kim.zkp.quick.monitor.MonitorSql;
import kim.zkp.quick.orm.exception.ConnectionException;

public class ConnectionPool {
	private static final Log log = LogFactory.get();
	private final Long CONNECTION_SURVIVE_TIME = 30*60*1000L;
	private JDBCConfig jdbcConfig;
	private final ReentrantLock lock = new ReentrantLock(true);
	private Condition get = lock.newCondition();
//	private Condition recycle = lock.newCondition();
	private final List<Connection> unUsedConn = new LinkedList<Connection>();
	private final List<Connection> usedConn = new LinkedList<Connection>();
	private volatile AtomicInteger connSize = new AtomicInteger(0);
//	private volatile ThreadLocal<Long> waitTime = new ThreadLocal<Long>();
	/**用于处理空闲、过期连接*/
	private final ScheduledExecutorService clearService = Executors.newSingleThreadScheduledExecutor();
	
	public ConnectionPool(JDBCConfig jdbcConfig) {
		this.jdbcConfig = jdbcConfig;
		initConnectionPool(jdbcConfig.getInitialPoolSize());
		if (jdbcConfig.getExecuteTimeMonitor()) {//开启sql执行耗时监控
			MonitorSql.start(jdbcConfig.getMaxExecuteTimeFilePath());
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
			log.error("Initialize db connection error",e);
			throw new ConnectionException(e);
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
			Connection qcw = new ConnectionWrapper(conn,this);
			log.debug("Create db connection：{} success",qcw);
			return qcw;
		} catch (SQLException e) {
			log.error("Create db connection error",e);
			throw new ConnectionException("Create db connection error",e);
		}finally{
			lock.unlock();
		}
	}
	public Connection getConnection(){
		lock.lock();
		try {
			if (unUsedConn.size() > 0) {
				Connection conn = unUsedConn.get(0);
				ConnectionWrapper qcw = (ConnectionWrapper) conn;
				if(qcw.getSurviveTime() > CONNECTION_SURVIVE_TIME){
					qcw.destroy();
					unUsedConn.remove(conn);
					connSize.decrementAndGet();
					return getConnection();
				}
				qcw.setLastUsedTime(System.currentTimeMillis());
				unUsedConn.remove(conn);
				usedConn.add(conn);
				return conn;
			}
			if (connSize.get() < jdbcConfig.getMaxPoolSize()) {
				Connection conn = createConnection();
				usedConn.add(conn);
				return conn;
			}
			boolean isSignal = get.await(jdbcConfig.getMaxWaitTime(), TimeUnit.MILLISECONDS);
			if (isSignal) {
				return getConnection();
			}
			throw new ConnectionException("当前数据库连接已达上限，无法再创建连接");
//			boolean isReleaseLock= true;
//			isReleaseLock = false;
//			lock.unlock(); //等待连接期间释放锁
//			while(true){
//				if (waitTime.get() == null) {
//					waitTime.set(0L);
//				}
//				if (waitTime.get() > jdbcConfig.getMaxWaitTime()) {
//					waitTime.set(0L);
//					throw new ConnectionException("当前数据库连接已达上线，无法再创建连接");
//				}
//				waitTime.set(waitTime.get()+jdbcConfig.getOncePollTime());
//				wait(jdbcConfig.getOncePollTime());
//				return getConnection();
//			}
			
		} catch (Exception e) {
			log.error("Get connection error",e);
			throw new ConnectionException("Get connection error",e);
		}finally {
			lock.unlock();
//			if (isReleaseLock) {
//				lock.unlock();
//			}
		}
	}
	public Connection getConnection(String username, String password) throws SQLException {
		if (jdbcConfig.getUsername().equals(username) && jdbcConfig.getPassword().equals(password)) {
			return getConnection();
		}
		throw new ConnectionException("Username or password error");
	}
//	private void wait(int mSeconds) {  
//        try {  
//            Thread.sleep(mSeconds);  
//        } catch (InterruptedException e) {  
//        }  
//    }  
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
						ConnectionWrapper qcw = (ConnectionWrapper) c;
						if (getIdleTime(qcw.getLastUsedTime()) < jdbcConfig.getMaxIdleTime()) 
							return false;
						try {
							qcw.destroy();
						} catch (Exception e) {
							log.error("关闭连接出现异常", e);
						}
						connSize.decrementAndGet();
						get.signal();
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
	 * description   : 清理已使用但未归还连接，每五分钟执行一次，若获取连接后，过了30分钟还未归还，则关闭此连接。
	 *                 规避连接泄露风险
	 * @return       : void
	 * @param        : 
	 * modified      : zhukaipeng ,  2017年8月17日 
	 * @see          : 
	 * *******************************************
	 */
	public void timerClearUsedConnection(){
		int interval = 5*60*1000;
		int idle = 30*60*1000;
				
		clearService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					lock.lock();
					log.info("开始清理已使用未归还连接。【已创建连接：{}，已使用：{}，空闲：{}】",connSize.get(),usedConn.size(),unUsedConn.size());
					usedConn.removeIf(c -> {
						ConnectionWrapper qcw = (ConnectionWrapper) c;
						if (getIdleTime(qcw.getLastUsedTime()) < idle) {
							return false;
						}
						try {
							qcw.destroy();
						} catch (Exception e) {
							log.error("关闭连接出现异常", e);
						}
						connSize.decrementAndGet();
						get.signal();
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
			get.signal();
//			log.debug("回收连接：{}成功",conn);
//			log.debug("当前连接情况【已创建：{}，已使用：{}，空闲：{}】",connSize.get(),usedConn.size(),unUsedConn.size());
		} catch (Exception e) {
			log.error("回收连接出现异常", e);
			throw new ConnectionException("回收连接出现异常",e);
		}finally{
			lock.unlock();
		}
	}
}