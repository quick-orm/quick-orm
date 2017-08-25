package com.z.quick.orm;

import java.util.List;

public interface SqlExecute {

	int save(Object o);

	int update(Object o);

	Object get(Object o);

	Object get(Object o, Class<?> clzz);

	List<Object> list(Object o);

	List<Object> list(Object o, Class<?> clzz);

	Object get(String sql, Class<?> clzz, Object[] params);

	List<Object> list(String sql, Class<?> clzz, Object[] params);

	int save(String sql, Object[] params);

	int update(String sql, Object[] params);

	int delete(Object o);

}