package com.z.quick.orm.dao;

import java.util.List;
import java.util.concurrent.Future;

public interface Dao<T> {
	
	int save(T t);

	int delete(Object o);
	
	int update(T t);

	T get(T t);

	T get(T t, Class<?> clzz);

	List<T> find(T t);

	List<T> find(T t, Class<?> clzz);
	
	Object get(String sql, Class<?> clzz, Object[] params);

	List<Object> list(String sql, Class<?> clzz, Object[] params);

	int save(String sql, Object[] params);

	int update(String sql, Object[] params);
	
	int delete(String sql, Object[] params);

	Future<Integer> asyncSave(T t);

	Future<Integer> asyncUpdate(T t);

	Future<T> asyncGet(T t);

	Future<T> asyncGet(T t, Class<?> clzz);

	Future<List<Object>> asyncList(T t);

	Future<List<Object>> asyncList(T t, Class<?> clzz);
}
