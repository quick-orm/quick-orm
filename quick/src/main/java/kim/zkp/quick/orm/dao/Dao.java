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

import java.util.List;
import java.util.concurrent.Future;

import kim.zkp.quick.orm.model.Page;
//TODO 增加返回结果类型参数
public interface Dao<T> {
	
	int save(T t);

	int delete(T o);
	
	int update(T t);

	T get(T t);

	List<T> find(T t);

	int save(String sql, List<Object> params);

	int delete(String sql, List<Object> params);
	
	int update(String sql, List<Object> params);
	
	Object get(String sql, List<Object> params, Class<?> clzz);
	
	List<Object> list(String sql, List<Object> params, Class<?> clzz);
	
	Page<T> page(Object o);
	
	Page<Object> page(String countSql,String listSql, List<Object> params, Class<?> clzz);
	
	Future<Integer> ftSave(T t);

	Future<Integer> ftDelete(T t);
	
	Future<Integer> ftUpdate(T t);

	Future<T> ftGet(T t);

	Future<List<Object>> ftList(T t);
	
	Future<Integer> ftSave(String sql, List<Object> params);

	Future<Integer> ftDelete(String sql, List<Object> params);
	
	Future<Integer> ftUpdate(String sql, List<Object> params);
	
	Future<Object> ftGet(String sql, List<Object> params, Class<?> clzz);
	
	Future<List<Object>> ftList(String sql, List<Object> params, Class<?> clzz);
	
	Future<Page<Object>> ftPage(Object o);
	
	Future<Page<Object>> ftPage(String countSql,String listSql, List<Object> params, Class<?> clzz);

}