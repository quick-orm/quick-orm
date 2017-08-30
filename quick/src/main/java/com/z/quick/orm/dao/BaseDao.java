package com.z.quick.orm.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Future;

import com.z.quick.orm.session.Session;

@SuppressWarnings("unchecked")
public abstract class BaseDao<T> implements Dao<T> {

	private Class<T> genericityClass;
	
	private Session session;

	public BaseDao() {
		this.genericityClass = (Class<T>) getSuperClassGenricType(getClass(), 0);
		session = Session.getSession();
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
	public T get(String sql, List<Object> params) {
		return (T) Session.getSession().get(sql, genericityClass, params);
	}

	@Override
	public List<T> list(String sql, List<Object> params) {
		return (List<T>) Session.getSession().list(sql, genericityClass, params);
	}

	@Override
	public int save(String sql, List<Object> params) {
		return Session.getSession().save(sql, params);
	}

	@Override
	public int update(String sql, List<Object> params) {
		return Session.getSession().update(sql, params);
	}

	@Override
	public int delete(String sql, List<Object> params) {
		return Session.getSession().delete(sql, params);
	}

	@Override
	public Future<Integer> ftSave(T t) {
		return session.ftSave(t);
	}

	@Override
	public Future<Integer> ftDelete(T t) {
		return session.ftDelete(t);
	}

	@Override
	public Future<Integer> ftUpdate(T t) {
		return session.ftUpdate(t);
	}

	@Override
	public Future<T> ftGet(T t) {
		return (Future<T>) session.get(t);
	}

	@Override
	public Future<List<Object>> ftList(T t) {
		return session.ftList(t);
	}

	@Override
	public Future<Integer> ftSave(String sql, List<Object> params) {
		return session.ftSave(sql, params);
	}

	@Override
	public Future<Integer> ftDelete(String sql, List<Object> params) {
		return session.ftDelete(sql, params);
	}

	@Override
	public Future<Integer> ftUpdate(String sql, List<Object> params) {
		return session.ftUpdate(sql, params);
	}

	@Override
	public Future<T> ftGet(String sql, List<Object> params) {
		return (Future<T>) session.ftGet(sql, genericityClass, params);
	}

	@Override
	public Future<List<Object>> ftList(String sql, List<Object> params) {
		return session.ftList(sql, genericityClass, params);
	}

}
