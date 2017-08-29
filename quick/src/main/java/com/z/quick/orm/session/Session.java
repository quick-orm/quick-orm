package com.z.quick.orm.session;

import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
public class Session {

	private JDBCConfig jdbcConfig;
	private DataSource dataSource;
	private FutureSession future;
	private static final Session session = new Session();;
	/**
	 * 暂无对象管理容器，只支持单一数据源，后期优化，配置文件默认为 jdbc.setting
	 */
	private Session() {
		super();
		jdbcConfig = JDBCConfig.newInstance("jdbc.setting");
		this.dataSource = new QuickDataSource(jdbcConfig);
		this.future = new FutureSession(this, jdbcConfig.getAsyncPoolSize());
	}
	
	public static Session getSession(){
		return session;
	}
	public FutureSession getFuture() {
		return future;
	}
	public JDBCConfig getJdbcConfig() {
		return jdbcConfig;
	}
	
	/** ********************************************
	 * method name   : save 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#save(java.lang.Object)
	 * ********************************************/     
	
	public int save(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.SAVE, o);
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	
	public int delete(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.DELETE, o);
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	
	/** ********************************************
	 * method name   : update 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#update(java.lang.Object)
	 * ********************************************/     
	
	public int update(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.UPDATE, o);
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	/** ********************************************
	 * method name   : get 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#get(java.lang.Object)
	 * ********************************************/     
	
	public Object get(Object o) {
		return this.get(o, o.getClass());
	}
	
	/** ********************************************
	 * method name   : get 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#get(java.lang.Object, java.lang.Class)
	 * ********************************************/     
	
	public Object get(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.GET, o);
		return ConnectionProcessor.get(getConnection(), sqlInfo,clzz);
	}
	
	/** ********************************************
	 * method name   : find 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#find(java.lang.Object)
	 * ********************************************/     
	
	public List<Object> list(Object o) {
		return this.list(o, o.getClass());
	}
	
	/** ********************************************
	 * method name   : find 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#find(java.lang.Object, java.lang.Class)
	 * ********************************************/     
	
	public List<Object> list(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(SqlBuilder.SBType.LIST, o);
		return ConnectionProcessor.list(getConnection(), sqlInfo,clzz);
	}
	
	
	public Object get(String sql,Class<?> clzz,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.get(getConnection(), sqlInfo, clzz);
	}
	
	public List<Object> list(String sql,Class<?> clzz,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.list(getConnection(), sqlInfo, clzz);
	}
	
	public int save(String sql,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	public int update(String sql,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	
	public int delete(String sql, Object... params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionProcessor.update(getConnection(), sqlInfo);
	}
	private Connection getConnection(){
		return ConnectionProcessor.getConnection(dataSource);
	}
	
	public void start(){
		ConnectionProcessor.setAutoCommit(getConnection(), false);
	}
	public void rollback(){
		ConnectionProcessor.rollback(getConnection());
	}
	public void commit(){
		ConnectionProcessor.commit(getConnection());
	}
	public void close(){
		ConnectionProcessor.close(getConnection());
	}

	
	
}
