package com.z.quick.orm.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Future;

import com.z.quick.orm.session.Session;

@SuppressWarnings("unchecked")
public abstract class BaseDao<T> implements Dao<T> {

	private Class<T> genericityClass;

	public BaseDao() {
		this.genericityClass = (Class<T>) getSuperClassGenricType(getClass(), 0);
	}

	private static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}

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
	public List<T> find(T t) {
		return (List<T>) Session.getSession().list(t);
	}

	@Override
	public T get(String sql, Object...params) {
		return (T) Session.getSession().get(sql, genericityClass, params);
	}

	@Override
	public List<T> list(String sql, Object...params) {
		return (List<T>) Session.getSession().list(sql, genericityClass, params);
	}

	@Override
	public int save(String sql, Object...params) {
		return Session.getSession().save(sql, params);
	}

	@Override
	public int update(String sql, Object...params) {
		return Session.getSession().update(sql, params);
	}

	@Override
	public int delete(String sql, Object...params) {
		return Session.getSession().delete(sql, params);
	}

	@Override
	public Future<Integer> asyncSave(T t) {
		return Session.getSession().getFuture().save(t);
	}

	@Override
	public Future<Integer> asyncDelete(T t) {
		return Session.getSession().getFuture().delete(t);
	}

	@Override
	public Future<Integer> asyncUpdate(T t) {
		return Session.getSession().getFuture().update(t);
	}

	@Override
	public Future<T> asyncGet(T t) {
		return (Future<T>) Session.getSession().getFuture().get(t);
	}

	@Override
	public Future<List<Object>> asyncList(T t) {
		return Session.getSession().getFuture().list(t);
	}

	@Override
	public Future<Integer> asyncSave(String sql, Object...params) {
		return Session.getSession().getFuture().save(sql, params);
	}

	@Override
	public Future<Integer> asyncDelete(String sql, Object...params) {
		return Session.getSession().getFuture().delete(sql, params);
	}

	@Override
	public Future<Integer> asyncUpdate(String sql, Object...params) {
		return Session.getSession().getFuture().update(sql, params);
	}

	@Override
	public Future<T> asyncGet(String sql, Object...params) {
		return (Future<T>) Session.getSession().getFuture().get(sql, genericityClass, params);
	}

	@Override
	public Future<List<Object>> asyncList(String sql, Object...params) {
		return Session.getSession().getFuture().list(sql, genericityClass, params);
	}

}
