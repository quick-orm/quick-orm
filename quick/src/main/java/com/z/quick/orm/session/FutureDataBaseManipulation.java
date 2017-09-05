package com.z.quick.orm.session;

import java.util.List;
import java.util.concurrent.Future;

import com.z.quick.orm.model.Page;

public interface FutureDataBaseManipulation {

	Future<Integer> ftSave(Object o);

	Future<Integer> ftDelete(Object o);

	Future<Integer> ftUpdate(Object o);

	Future<Object> ftGet(Object o);

	Future<Object> ftGet(Object o, Class<?> clzz);

	Future<List<Object>> ftList(Object o);

	Future<List<Object>> ftList(Object o, Class<?> clzz);

	Future<Integer> ftSave(String sql, List<Object> params);

	Future<Integer> ftDelete(String sql, List<Object> params);
	
	Future<Integer> ftUpdate(String sql, List<Object> params);

	Future<Object> ftGet(String sql, List<Object> params, Class<?> clzz);

	Future<List<Object>> ftList(String sql, List<Object> params, Class<?> clzz);
	
	Future<Page<Object>> ftPage(Object o);
	
	Future<Page<Object>> ftPage(Object o, Class<?> clzz);
	
	Future<Page<Object>> ftPage(String countSql,String listSql, List<Object> params, Class<?> clzz);


}