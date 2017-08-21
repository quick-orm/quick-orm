package com.z.quick.orm;

import java.util.List;
import java.util.concurrent.Future;

public interface SQLAsyncExecute {

	Future<Integer> save(Object o);

	Future<Integer> update(Object o);

	Future<Object> get(Object o);

	Future<Object> get(Object o, Class<?> clzz);

	Future<List<Object>> find(Object o);

	Future<List<Object>> find(Object o, Class<?> clzz);

}