package com.z.quick.orm;

import java.util.List;
import java.util.concurrent.Future;

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

	@Override
	public Future<Integer> asyncSave(T t) {
		return Session.getSession().getSqlAsyncExecute().save(t);
	}

	@Override
	public Future<Integer> asyncUpdate(T t) {
		return Session.getSession().getSqlAsyncExecute().update(t);
	}

	@Override
	public Future<T> asyncGet(T t) {
		return (Future<T>) Session.getSession().getSqlAsyncExecute().get(t);
	}

	@Override
	public Future<T> asyncGet(T t, Class<?> clzz) {
		return (Future<T>) Session.getSession().getSqlAsyncExecute().get(t, clzz);
	}

	@Override
	public Future<List<Object>> asyncFind(T t) {
		return Session.getSession().getSqlAsyncExecute().find(t);
	}

	@Override
	public Future<List<Object>> asyncFind(T t, Class<?> clzz) {
		return Session.getSession().getSqlAsyncExecute().find(t, clzz);
	}

}
