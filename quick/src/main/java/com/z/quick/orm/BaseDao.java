package com.z.quick.orm;

import java.util.List;

public abstract class BaseDao<T> implements Dao<T> {
	
	@Override
	public int save(T t) {
		return Session.getSession().save(t);
	}

	@Override
	public int update(T t) {
		return Session.getSession().update(t);
	}

	@Override
	public T get(T t) {
		return (T) Session.getSession().get(t);
	}

	@Override
	public T get(T t, Class<?> clzz) {
		return (T) Session.getSession().get(t, clzz);
	}

	@Override
	public List<T> find(T t) {
		return (List<T>) Session.getSession().find(t);
	}

	@Override
	public List<T> find(T t, Class<?> clzz) {
		return (List<T>) Session.getSession().find(t, clzz);
	}

}
