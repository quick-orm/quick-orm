package com.z.quick.orm.session;

import java.util.List;
import java.util.concurrent.Future;

public interface FutureDataBaseManipulation<T> {

	Future<Integer> ftSave(Object o);

	Future<Integer> ftDelete(Object o);

	Future<Integer> ftUpdate(Object o);

	Future<T> ftGet(Object o);

	Future<T> ftGet(Object o, Class<?> clzz);

	Future<List<T>> ftList(Object o);

	Future<List<T>> ftList(Object o, Class<?> clzz);

	Future<Integer> ftSave(String sql, List<Object> params);

	Future<Integer> ftDelete(String sql, List<Object> params);
	
	Future<Integer> ftUpdate(String sql, List<Object> params);

	Future<T> ftGet(String sql, Class<?> clzz, List<Object> params);

	Future<List<T>> ftList(String sql, Class<?> clzz, List<Object> params);


}