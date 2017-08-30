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
	
	Future<Integer> ftSave(T t);

	Future<Integer> ftDelete(T t);
	
	Future<Integer> ftUpdate(T t);

	Future<T> ftGet(T t);

	Future<List<Object>> ftList(T t);
	
	Future<Integer> ftSave(String sql, Object...params);

	Future<Integer> ftDelete(String sql, Object...params);
	
	Future<Integer> ftUpdate(String sql, Object...params);
	
	Future<T> ftGet(String sql, Object...params);
	
	Future<List<Object>> ftList(String sql, Object...params);

}









