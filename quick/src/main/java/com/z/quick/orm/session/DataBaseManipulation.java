package com.z.quick.orm.session;

import java.util.List;

import com.z.quick.orm.connection.JDBCConfig;

public interface DataBaseManipulation {
	
	JDBCConfig getJdbcConfig();

	FutureDataBaseManipulation getFuture();

	int save(Object o);

	int delete(Object o);

	int update(Object o);

	Object get(Object o);

	Object get(Object o, Class<?> clzz);

	List<Object> list(Object o);

	List<Object> list(Object o, Class<?> clzz);

	int save(String sql, Object... params);

	int delete(String sql, Object... params);
	
	int update(String sql, Object... params);

	Object get(String sql, Class<?> clzz, Object... params);

	List<Object> list(String sql, Class<?> clzz, Object... params);

	void start();

	void rollback();

	void commit();

	void close();

}