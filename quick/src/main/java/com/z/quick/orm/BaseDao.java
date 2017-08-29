package com.z.quick.orm;

import java.util.List;
import java.util.concurrent.Future;
@SuppressWarnings("unchecked")
public abstract class BaseDao<T> implements Dao<T> {
	
	@Override
	public int save(T t) {
		return Session.getSession().save(t);
	}
	
	@Override
	public int delete(Object o) {
		return Session.getSession().delete(o);
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
		return (List<T>) Session.getSession().list(t);
	}

	@Override
	public List<T> find(T t, Class<?> clzz) {
		return (List<T>) Session.getSession().list(t, clzz);
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
	public Future<List<Object>> asyncList(T t) {
		return Session.getSession().getSqlAsyncExecute().list(t);
	}

	@Override
	public Future<List<Object>> asyncList(T t, Class<?> clzz) {
		return Session.getSession().getSqlAsyncExecute().list(t, clzz);
	}

	@Override
	public Object get(String sql, Class<?> clzz, Object[] params) {
		return Session.getSession().get(sql, clzz, params);
	}

	@Override
	public List<Object> list(String sql, Class<?> clzz, Object[] params) {
		return Session.getSession().list(sql, clzz, params);
	}

	@Override
	public int save(String sql, Object[] params) {
		return Session.getSession().save(sql, params);
	}

	@Override
	public int update(String sql, Object[] params) {
		return Session.getSession().update(sql, params);
	}

	@Override
	public int delete(String sql, Object[] params) {
		return Session.getSession().delete(sql, params);
	}

}
