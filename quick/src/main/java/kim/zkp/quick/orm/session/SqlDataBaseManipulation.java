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
 * class       :  SqlDataBaseManipulation
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  sql同步操作数据库接口
 * @see        :  *
 */
public interface SqlDataBaseManipulation {
	/**
	 * method name   : sqlSave 
	 * description   : 保存
	 * @return       : int
	 * @param        : @param sql 保存SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	int sqlSave(String sql, Object ... params);
	/**
	 * method name   : sqlSave 
	 * description   : 保存
	 * @return       : int
	 * @param        : @param sql
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	int sqlSave(String sql);
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
	int sqlDelete(String sql, Object ... params);
	/**
	 * method name   : sqlDelete 
	 * description   : 删除
	 * @return       : int
	 * @param        : @param sql
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	int sqlDelete(String sql);
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
	int sqlUpdate(String sql, Object ... params);
	
	/**
	 * method name   : update 
	 * description   : 
	 * @return       : int
	 * @param        : @param sql 更新SQL
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	int sqlUpdate(String sql);
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
	Object sqlGet(String sql, Class<?> clzz, Object ... params);
	/**
	 * method name   : sqlGet 
	 * description   : 查询
	 * @return       : Object
	 * @param        : @param sql
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Object sqlGet(String sql, Class<?> clzz);
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
	List<Object> sqlList(String sql, Class<?> clzz, Object ... params);
	/**
	 * method name   : sqlList 
	 * description   : 列表查询
	 * @return       : List<Object>
	 * @param        : @param sql
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	List<Object> sqlList(String sql, Class<?> clzz);
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
	Page<Object> sqlPage(String countSql,String listSql, Class<?> clzz, Object ... params);
	/**
	 * method name   : sqlPage 
	 * description   : 分页查询
	 * @return       : Page<Object>
	 * @param        : @param countSql
	 * @param        : @param listSql
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Page<Object> sqlPage(String countSql,String listSql, Class<?> clzz);
	

}