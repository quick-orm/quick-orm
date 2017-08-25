package com.z.quick.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import com.z.quick.orm.connection.ConnectionUtils;
import com.z.quick.orm.connection.JDBCConfig;
import com.z.quick.orm.connection.QuickDataSource;
import com.z.quick.orm.exception.ConnectionException;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.sql.builder.ISqlBuilder;
import com.z.quick.orm.sql.builder.SqlBuilderProcessor;
public class Session implements SqlExecute {

	private JDBCConfig jdbcConfig;
	private DataSource dataSource;
	private SqlAsyncExecute sqlAsyncExecute;
	private static final Session session = new Session();;
	
	/**
	 * 暂无对象管理容器，只支持单一数据源，后期优化，配置文件默认为 jdbc.setting
	 */
	private Session() {
		super();
		jdbcConfig = JDBCConfig.newInstance("jdbc.setting");
		this.dataSource = new QuickDataSource(jdbcConfig);
		this.sqlAsyncExecute = new FutureSqlAsyncExecute(this, jdbcConfig.getAsyncPoolSize());
	}
	
	public static Session getSession(){
		return session;
	}
	public SqlAsyncExecute getSqlAsyncExecute() {
		return sqlAsyncExecute;
	}
	public JDBCConfig getJdbcConfig() {
		return jdbcConfig;
	}
	
	/** ********************************************
	 * method name   : save 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#save(java.lang.Object)
	 * ********************************************/     
	@Override
	public int save(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(ISqlBuilder.SBType.SAVE, o);
		return ConnectionUtils.update(getConnection(), sqlInfo);
	}
	
	@Override
	public int delete(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(ISqlBuilder.SBType.DELETE, o);
		return ConnectionUtils.update(getConnection(), sqlInfo);
	}
	
	
	/** ********************************************
	 * method name   : update 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#update(java.lang.Object)
	 * ********************************************/     
	@Override
	public int update(Object o) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(ISqlBuilder.SBType.UPDATE, o);
		return ConnectionUtils.update(getConnection(), sqlInfo);
	}
	
	/** ********************************************
	 * method name   : get 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#get(java.lang.Object)
	 * ********************************************/     
	@Override
	public Object get(Object o) {
		return this.get(o, o.getClass());
	}
	
	/** ********************************************
	 * method name   : get 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#get(java.lang.Object, java.lang.Class)
	 * ********************************************/     
	@Override
	public Object get(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(ISqlBuilder.SBType.GET, o);
		return ConnectionUtils.get(getConnection(), sqlInfo,clzz);
	}
	
	/** ********************************************
	 * method name   : find 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#find(java.lang.Object)
	 * ********************************************/     
	@Override
	public List<Object> list(Object o) {
		return this.list(o, o.getClass());
	}
	
	/** ********************************************
	 * method name   : find 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#find(java.lang.Object, java.lang.Class)
	 * ********************************************/     
	@Override
	public List<Object> list(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(ISqlBuilder.SBType.LIST, o);
		return ConnectionUtils.list(getConnection(), sqlInfo,clzz);
	}
	
	@Override
	public Object get(String sql,Class<?> clzz,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionUtils.get(getConnection(), sqlInfo, clzz);
	}
	@Override
	public List<Object> list(String sql,Class<?> clzz,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionUtils.list(getConnection(), sqlInfo, clzz);
	}
	@Override
	public int save(String sql,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionUtils.update(getConnection(), sqlInfo);
	}
	@Override
	public int update(String sql,Object...params) {
		SqlInfo sqlInfo = new SqlInfo(sql, new LinkedList<Object>(Arrays.asList(params)));
		return ConnectionUtils.update(getConnection(), sqlInfo);
	}
	private Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new ConnectionException("Get db connection error",e);
		}
	}
	
	
}
