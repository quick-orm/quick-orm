package com.z.quick.orm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.z.quick.orm.connection.ConnectionUtils;
import com.z.quick.orm.connection.QuickDataSource;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.sql.builder.ISqlBuilder;
import com.z.quick.orm.sql.builder.SqlBuilderProcessor;

public class Session implements SQLExecute {

	private DataSource dataSource;
	
	private SQLExecute asyncSQLExecute;  
	
	private static final Session session = new Session();;
	
	private Session() {
		super();
		this.dataSource = new QuickDataSource("jdbc.setting");
		InvocationHandler invocationHandler = new FutureInvocationHandler(this); 
		asyncSQLExecute = (SQLExecute)Proxy.newProxyInstance(this.getClass().getClassLoader(),  
	            this.getClass().getInterfaces(), invocationHandler);
	}
	public static Session getSession(){
		return session;
	}
	public SQLExecute getAsyncSQLExecute(){
		return asyncSQLExecute;
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
	public List<Object> find(Object o) {
		return this.find(o, o.getClass());
	}
	
	/** ********************************************
	 * method name   : find 
	 * modified      : zhukaipeng ,  2017年8月17日
	 * @see          : @see com.z.quick.orm.SQLHandler#find(java.lang.Object, java.lang.Class)
	 * ********************************************/     
	@Override
	public List<Object> find(Object o,Class<?> clzz) {
		SqlInfo sqlInfo = SqlBuilderProcessor.getSql(ISqlBuilder.SBType.FIND, o);
		return ConnectionUtils.find(getConnection(), sqlInfo,clzz);
	}
	
	private Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("获取数据库连接出错",e);
		}
	}
	public Object executeSql(String namespace,Class<?> clzz) {

		return null;
	}
	
	

	public void updateSql() {

	}

}
