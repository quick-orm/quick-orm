package com.z.quick.orm.dao;

import java.util.List;
import java.util.concurrent.Future;

public interface Dao<T> {
	
	int save(T t);

	int delete(Object o);
	
	int update(T t);

	T get(T t);

	List<T> find(T t);

	int save(String sql, Object...params);

	int delete(String sql, Object...params);
	
	int update(String sql, Object...params);
	
	T get(String sql, Object...params);
	
	List<T> list(String sql, Object...params);
	
	Future<Integer> asyncSave(T t);

	Future<Integer> asyncDelete(T t);
	
	Future<Integer> asyncUpdate(T t);

	Future<T> asyncGet(T t);

	Future<List<Object>> asyncList(T t);
	
	Future<Integer> asyncSave(String sql, Object...params);

	Future<Integer> asyncDelete(String sql, Object...params);
	
	Future<Integer> asyncUpdate(String sql, Object...params);
	
	Future<T> asyncGet(String sql, Object...params);
	
	Future<List<Object>> asyncList(String sql, Object...params);

}









