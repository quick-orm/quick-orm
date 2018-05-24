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

package kim.zkp.quick.orm.sql.builder;

import java.util.List;

import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.model.Schema;
import kim.zkp.quick.orm.util.AnnotationSqlBuilderUtils;
import kim.zkp.quick.orm.util.ObjectSqlBuilderUtils;

public abstract class AbstractSqlBuilder implements SqlBuilder {
	protected static final String SAVE_TEMPLATE = "insert into #tableName(#insertParam) values(#insertValue)";
	protected static final String DELETE_TEMPLATE = "delete from #tableName #condition";
	protected static final String UPDATE_TEMPLATE = "update #tableName set #modif #primaryKey";
	protected static final String GET_TEMPLATE = "select #select from #tableName as #alias #join #condition";
	protected static final String LIST_TEMPLATE = "select #select from #tableName as #alias #join #condition";
	protected static final String PAGE_COUNT_TEMPLATE = "select count(1) from #tableName as #alias #join #condition";
	
	/**
	 * ********************************************
	 * method name   : getTableName 
	 * description   : 获取表名
	 * @return       : String
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年8月14日  下午2:38:16
	 * @see          : 
	 * *******************************************
	 */
	protected String getTableName(Object o){
		String tableName = ObjectSqlBuilderUtils.getTableName(o);
		if (tableName!=null) {
			return tableName;
		}else {
			if (o instanceof Schema) {
				throw new SqlBuilderException("TableName is null");
			}
		}
		return AnnotationSqlBuilderUtils.getTableName(o);
	}
	protected String getAlias(Object o){
		return ClassCache.getAlias(o.getClass());
	}
	
	protected String getJoin(Object o){
		return AnnotationSqlBuilderUtils.getJoin(o);
	}
	
	protected String getSelect(Object o){
		String select = ObjectSqlBuilderUtils.getSelect(o);
		if (select != null) {
			return select;
		}
		select = AnnotationSqlBuilderUtils.getSelect(o);
		if (select == null || "".equals(select)) {
			throw new SqlBuilderException("Select list is null");
		}
		return select;
	}
	protected void getInsert(Object o,StringBuilder insertParam,StringBuilder insertValue,List<Object> valueList){
		ObjectSqlBuilderUtils.getInsert(o, insertParam, insertValue, valueList);
		AnnotationSqlBuilderUtils.getInsert(o, insertParam, insertValue, valueList);;
		if (valueList.size()<=0) {
			throw new SqlBuilderException("Insert param is null");
		}
	}
	protected String getCondition(Object o,List<Object> valueList){
		StringBuilder condition = new StringBuilder();
		ObjectSqlBuilderUtils.getCondition(o, condition,valueList);
		AnnotationSqlBuilderUtils.getCondition(o,condition, valueList);
		return condition.toString();
	}
	protected String getPrimaryKey(Object o,List<Object> valueList){
		String condition = ObjectSqlBuilderUtils.getPrimaryKey(o, valueList);
		if (condition != null) {
			return condition;
		}
		condition = AnnotationSqlBuilderUtils.getPrimaryKey(o, valueList);
		return condition;
	}
	protected String getModif(Object o,List<Object> valueList){
		StringBuilder modif = new StringBuilder();
		ObjectSqlBuilderUtils.getModif(o,modif, valueList);
		AnnotationSqlBuilderUtils.getModif(o,modif, valueList);
		if (modif.length()<=0) {
			throw new SqlBuilderException("Modif param is null");
		}
		return modif.substring(0,modif.lastIndexOf(","));
	}
	protected String getOrderBy(Object o){
		String orderBy = ObjectSqlBuilderUtils.getOrderBy(o);
		if (orderBy != null) {
			return orderBy;
		}
		return AnnotationSqlBuilderUtils.getOrderBy(o);
	}
	
}