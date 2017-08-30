package com.z.quick.orm.session;

import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import com.z.quick.orm.connection.ConnectionProcessor;
import com.z.quick.orm.connection.JDBCConfig;
import com.z.quick.orm.connection.QuickDataSource;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.sql.builder.SqlBuilder;
import com.z.quick.orm.sql.builder.SqlBuilderProcessor;
/**
 * class       :  Session
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  如何抽象？支持数据库、redis等操作
 * @see        :  *
 */
public class Session implements DataBaseManipulation<Object>,FutureDataBaseManipulation<Object> {

	private JDBCConfig jdbcConfig;
	private DataSource dataSource;
	private final ExecutorService threadPool;
	private static final Session session = new Session();
	/**
	 * 暂无对象管理容器，只支持单一数据源，后期优化，配置文件默认为 jdbc.setting
	 */
	private Session() {
		super();
		jdbcConfig = JDBCConfig.newInstance("jdbc.setting");
		this.dataSource = new QuickDataSource(jdbcConfig);
		threadPool = Executors.newFixedThreadPool(jdbcConfig.getAsyncPoolSize());
	}
	
	public static Session getSession(){
		return session;
	}
	
	public JDBCConfig getJdbcConfig() {
		return jdbcConfig;
	}
	
	@Override
	public int save(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.SAVE, o);
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int delete(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.DELETE, o);
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int update(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.UPDATE, o);
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public Object get(Object o) {
		return this.get(o, o.getClass());
	}
	
	@Override
	public Object get(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.GET, o);
		return ConnectionProcessor.get(getConnection(), sqlInfo,clzz);
	}

	@Override
	public List<Object> list(Object o) {
		return this.list(o, o.getClass());
	}
	
	@Override
	public List<Object> list(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.LIST, o);
		return ConnectionProcessor.list(getConnection(), sqlInfo,clzz);
	}
	
	@Override
	public int save(String sql,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int delete(String sql, Object... params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int update(String sql,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	@Override
	public Object get(String sql,Class<?> clzz,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.get(getConnection(), sqlInfo, clzz);
	}
	
	@Override
	public List<Object> list(String sql,Class<?> clzz,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.list(getConnection(), sqlInfo, clzz);
	}
	
	private Connection getConnection(){
		return ConnectionProcessor.getConnection(dataSource);
	}
	
	@Override
	public void start(){
		ConnectionProcessor.setAutoCommit(getConnection(), false);
	}
	
	@Override
	public void rollback(){
		ConnectionProcessor.rollback(getConnection());
	}
	
	@Override
	public void commit(){
		ConnectionProcessor.commit(getConnection());
	}
	
	@Override
	public void close(){
		ConnectionProcessor.close(getConnection());
	}
	

	@Override
	public Future<Integer> ftSave(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return save(o);
			}
		});
	}

	@Override
	public Future<Integer> ftDelete(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return delete(o);
			}
		});
	}

	@Override
	public Future<Integer> ftUpdate(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return update(o);
			}
		});
	}

	@Override
	public Future<Object> ftGet(Object o) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return get(o);
			}
		});
	}

	@Override
	public Future<Object> ftGet(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return get(o, clzz);
			}
		});
	}

	@Override
	public Future<List<Object>> ftList(Object o) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return list(o);
			}
		});
	}

	@Override
	public Future<List<Object>> ftList(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return list(o, clzz);
			}
		});
	}

	@Override
	public Future<Integer> ftSave(String sql, Object... params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return save(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> ftDelete(String sql, Object... params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return update(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> ftUpdate(String sql, Object... params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return update(sql, params);
			}
		});
	}

	@Override
	public Future<Object> ftGet(String sql, Class<?> clzz, Object... params) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return get(sql, clzz, params);
			}
		});
	}

	@Override
	public Future<List<Object>> ftList(String sql, Class<?> clzz, Object... params) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return list(sql, clzz, params);
			}
		});
	}

	
	
}
