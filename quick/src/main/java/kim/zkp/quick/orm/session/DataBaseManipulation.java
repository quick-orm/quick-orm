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

package kim.zkp.quick.orm.session;

import java.util.List;

import kim.zkp.quick.orm.model.Page;
/**
 * class       :  DataBaseManipulation
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  同步数据库操作
 * @see        :  *
 */
public interface DataBaseManipulation {
	/**
	 * method name   : save 
	 * description   : 保存
	 * @return       : int
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int save(Object o);
	/**
	 * method name   : delete 
	 * description   : 删除
	 * @return       : int
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int delete(Object o);
	/**
	 * method name   : update 
	 * description   : 更新
	 * @return       : int
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int update(Object o);
	/**
	 * method name   : get 
	 * description   : 查询
	 * @return       : Object
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Object get(Object o);
	/**
	 * method name   : get 
	 * description   : 查询
	 * @return       : Object
	 * @param        : @param o
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Object get(Object o, Class<?> clzz);
	/**
	 * method name   : list 
	 * description   : 列表查询
	 * @return       : List<Object>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	List<Object> list(Object o);
	/**
	 * method name   : list 
	 * description   : 列表查询
	 * @return       : List<Object>
	 * @param        : @param o
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	List<Object> list(Object o, Class<?> clzz);
	/**
	 * method name   : save 
	 * description   : 保存
	 * @return       : int
	 * @param        : @param sql 保存SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int save(String sql, List<Object> params);
	/**
	 * method name   : delete 
	 * description   : 删除
	 * @return       : int
	 * @param        : @param sql 删除SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int delete(String sql, List<Object> params);
	/**
	 * method name   : update 
	 * description   : 更新
	 * @return       : int
	 * @param        : @param sql 更新SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int update(String sql, List<Object> params);
	/**
	 * method name   : get 
	 * description   : 查询
	 * @return       : Object
	 * @param        : @param sql 查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Object get(String sql, List<Object> params, Class<?> clzz);
	/**
	 * method name   : list 
	 * description   : 列表查询
	 * @return       : List<Object>
	 * @param        : @param sql 查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	List<Object> list(String sql, List<Object> params, Class<?> clzz);
	/**
	 * method name   : page 
	 * description   : 分页查询
	 * @return       : Page<Object>
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Page<Object> page(Object o);
	/**
	 * method name   : page 
	 * description   : 分页查询
	 * @return       : Page<Object>
	 * @param        : @param o
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Page<Object> page(Object o, Class<?> clzz);
	/**
	 * method name   : page 
	 * description   : 分页查询
	 * @return       : Page<Object>
	 * @param        : @param countSql 统计SQL
	 * @param        : @param listSql 列表查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Page<Object> page(String countSql,String listSql, List<Object> params, Class<?> clzz);
	

}