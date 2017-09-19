/**
 * Copyright (c) 2017, ZhuKaipeng 朱开鹏 (2076528290@qq.com).

 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.zkp.quick.orm.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Future;

import kim.zkp.quick.orm.model.Page;
import kim.zkp.quick.orm.session.Session;

@SuppressWarnings({ "unchecked", "unused" })
public abstract class BaseDao<T> implements Dao<T> {

	private Class<T> genericityClass;
	
	protected Session session;

	public BaseDao() {
		this.genericityClass = (Class<T>) getSuperClassGenricType(getClass(), 0);
		session = Session.getDefaultSession();
	}

	private Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

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
		return session.save(t);
	}

	@Override
	public int delete(T t) {
		return session.delete(t);
	}

	@Override
	public int update(T t) {
		return session.update(t);
	}

	@Override
	public T get(T t) {
		return (T) session.get(t);
	}

	@Override
	public List<T> find(T t) {
		return (List<T>) session.list(t);
	}

	@Override
	public T get(String sql, List<Object> params, Class<?> clzz) {
		return (T) session.get(sql, params, clzz);
	}

	@Override
	public List<Object> list(String sql,List<Object> params, Class<?> clzz) {
		return session.list(sql, params, clzz);
	}

	@Override
	public int save(String sql, List<Object> params) {
		return session.save(sql, params);
	}

	@Override
	public int update(String sql, List<Object> params) {
		return session.update(sql, params);
	}

	@Override
	public int delete(String sql, List<Object> params) {
		return session.delete(sql, params);
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
	public Future<Object> ftGet(String sql,List<Object> params, Class<?> clzz) {
		return session.ftGet(sql, params, clzz);
	}

	@Override
	public Future<List<Object>> ftList(String sql,List<Object> params, Class<?> clzz) {
		return session.ftList(sql, params, clzz);
	}

	@Override
	public Page<T> page(Object o) {
		return (Page<T>) session.page(o);
	}

	@Override
	public Page<Object> page(String countSql, String listSql, List<Object> params, Class<?> clzz) {
		return session.page(countSql, listSql, params, clzz);
	}

	@Override
	public Future<Page<Object>> ftPage(Object o) {
		return session.ftPage(o);
	}

	@Override
	public Future<Page<Object>> ftPage(String countSql, String listSql, List<Object> params, Class<?> clzz) {
		return session.ftPage(countSql, listSql, params, clzz);
	}

}