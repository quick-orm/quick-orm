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
import java.util.concurrent.Future;

import kim.zkp.quick.orm.model.Page;
/**
 * class       :  FutureDataBaseManipulation
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  异步数据库操作接口
 * @see        :  *
 */
public interface SqlFutureDataBaseManipulation {
	/**
	 * method name   : ftSave 
	 * description   : 异步保存
	 * @return       : Future<Integer>
	 * @param        : @param sql 保存SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftSqlSave(String sql, Object ... params);
	/**
	 * method name   : ftSqlSave 
	 * description   : 异步保存
	 * @return       : Future<Integer>
	 * @param        : @param sql
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Future<Integer> ftSqlSave(String sql);
	/**
	 * method name   : ftDelete 
	 * description   : 异步删除
	 * @return       : Future<Integer>
	 * @param        : @param sql 删除SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftSqlDelete(String sql, Object ... params);
	/**
	 * method name   : ftSqlDelete 
	 * description   : 异步删除
	 * @return       : Future<Integer>
	 * @param        : @param sql
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Future<Integer> ftSqlDelete(String sql);
	/**
	 * method name   : ftUpdate 
	 * description   : 异步更新
	 * @return       : Future<Integer>
	 * @param        : @param sql 更新SQL
	 * @param        : @param params 参数
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Integer> ftSqlUpdate(String sql, Object ... params);
	/**
	 * method name   : ftSqlUpdate 
	 * description   : 异步更新
	 * @return       : Future<Integer>
	 * @param        : @param sql
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Future<Integer> ftSqlUpdate(String sql);
	/**
	 * method name   : ftGet 
	 * description   : 异步查询
	 * @return       : Future<Object>
	 * @param        : @param sql 查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Object> ftSqlGet(String sql, Class<?> clzz, Object ... params);
	/**
	 * method name   : ftSqlGet 
	 * description   : 异步查询
	 * @return       : Future<Object>
	 * @param        : @param sql
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Future<Object> ftSqlGet(String sql, Class<?> clzz);
	/**
	 * method name   : ftList 
	 * description   : 异步列表查询
	 * @return       : Future<List<Object>>
	 * @param        : @param sql 查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<List<Object>> ftSqlList(String sql, Class<?> clzz, Object ... params);
	/**
	 * method name   : ftSqlList 
	 * description   : 异步列表查询
	 * @return       : Future<List<Object>>
	 * @param        : @param sql
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Future<List<Object>> ftSqlList(String sql, Class<?> clzz);
	/**
	 * method name   : ftPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param countSql 统计SQL
	 * @param        : @param listSql 列表查询SQL
	 * @param        : @param params 参数
	 * @param        : @param clzz 返回值类型
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年9月15日
	 * @see          : *
	 */
	Future<Page<Object>> ftSqlPage(String countSql,String listSql, Class<?> clzz, Object ... params);
	/**
	 * method name   : ftSqlPage 
	 * description   : 异步分页查询
	 * @return       : Future<Page<Object>>
	 * @param        : @param countSql
	 * @param        : @param listSql
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2018年1月28日
	 */
	Future<Page<Object>> ftSqlPage(String countSql,String listSql, Class<?> clzz);


}