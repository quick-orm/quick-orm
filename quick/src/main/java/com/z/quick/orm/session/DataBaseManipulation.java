package com.z.quick.orm.session;

import java.util.List;

import com.z.quick.orm.connection.JDBCConfig;
import com.z.quick.orm.model.Page;

public interface DataBaseManipulation {
	
	JDBCConfig getJdbcConfig();

	int save(Object o);

	int delete(Object o);

	int update(Object o);

	Object get(Object o);

	Object get(Object o, Class<?> clzz);

	List<Object> list(Object o);

	List<Object> list(Object o, Class<?> clzz);

	int save(String sql, List<Object> params);

	int delete(String sql, List<Object> params);
	
	int update(String sql, List<Object> params);

	Object get(String sql, List<Object> params, Class<?> clzz);

	List<Object> list(String sql, List<Object> params, Class<?> clzz);
	
	Page<Object> page(Object o);
	
	Page<Object> page(Object o, Class<?> clzz);
	
	Page<Object> page(String countSql,String listSql, List<Object> params, Class<?> clzz);
	

	void start();

	void rollback();

	void commit();

	void close();

}