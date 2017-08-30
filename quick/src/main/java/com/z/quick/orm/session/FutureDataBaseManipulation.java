package com.z.quick.orm.session;

import java.util.List;
import java.util.concurrent.Future;

public interface FutureDataBaseManipulation {

	Future<Integer> save(Object o);

	Future<Integer> delete(Object o);

	Future<Integer> update(Object o);

	Future<Object> get(Object o);

	Future<Object> get(Object o, Class<?> clzz);

	Future<List<Object>> list(Object o);

	Future<List<Object>> list(Object o, Class<?> clzz);

	Future<Integer> save(String sql, Object...params);

	Future<Integer> delete(String sql, Object...params);
	
	Future<Integer> update(String sql, Object...params);

	Future<Object> get(String sql, Class<?> clzz, Object...params);

	Future<List<Object>> list(String sql, Class<?> clzz, Object...params);


}