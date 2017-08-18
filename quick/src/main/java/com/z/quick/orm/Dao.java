package com.z.quick.orm;

import java.util.List;

public interface Dao<T> {
	
	int save(T t);

	int update(T t);

	T get(T t);

	T get(T t, Class<?> clzz);

	List<T> find(T t);

	List<T> find(T t, Class<?> clzz);
}
