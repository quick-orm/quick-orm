package com.z.quick.orm;

import java.util.List;

public interface SQLExecute {

	int save(Object o);

	int update(Object o);

	Object get(Object o);

	Object get(Object o, Class<?> clzz);

	List<Object> find(Object o);

	List<Object> find(Object o, Class<?> clzz);

}