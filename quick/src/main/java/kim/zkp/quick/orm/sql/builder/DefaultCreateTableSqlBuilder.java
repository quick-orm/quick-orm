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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import kim.zkp.quick.orm.annotation.PrimaryKey;
import kim.zkp.quick.orm.annotation.Table;
import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.common.Constants;
import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.sql.SqlInfo;
/**
 * ******************  类说明  *********************
 * class       :  DefaultCreateTableSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成create table SQL
 * @see        :                        
 * ***********************************************
 */
public class DefaultCreateTableSqlBuilder extends AbstractSqlBuilder {
	private static final Log log = LogFactory.get();
	private static final String CREATE_TABLE_TEMPLATE = "create table #tableName(#columns primary key(#primaryKey))";
	@Override
	public SqlInfo builderSql(Object o) {
		Class<?> clzz = (Class<?>) o;
		String tableName = clzz.getAnnotation(Table.class).tableName();
		List<Field> fieldList = ClassCache.getInsert(clzz);
		StringBuffer columns = new StringBuffer();
		fieldList.forEach(f->{
			if (typeConvert(f) == null) {
				log.warn("未找到{}类型对应的数据库类型",f.getType());
				return;
			}
			columns.append(f.getName()).append(Constants.SPACE).append(typeConvert(f)).append(",");
		});
		fieldList = ClassCache.getPrimaryKey(clzz);
		StringBuffer primaryKey = new StringBuffer();
		if (fieldList.size() == 0) {
			throw new SqlBuilderException("No primaryKey,Please configure it in Po.");
		}
		fieldList.forEach(f->{
			primaryKey.append(f.getName()).append(",");
		});
		primaryKey.deleteCharAt(primaryKey.lastIndexOf(","));
		
		String sql = CREATE_TABLE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#columns", columns);
		sql = sql.replace("#primaryKey", primaryKey);
		return new SqlInfo(sql, new ArrayList<>());
	}
	
	public String typeConvert(Field f){
		Class<?> type = f.getType();
		String value = null;
		if (String.class == type) {
			value = "varchar(64)";
		}else if(Boolean.class == type){
			value = "smallint";
		}else if(Integer.class == type){
			value = "integer";
		}else if(Short.class == type){
			value = "smallint";
		}else if(Double.class == type || Float.class == type){
			value = "double";
		}else if(BigDecimal.class == type){
			value = "decimal(12,2)";
		}else if(Long.class == type){
			value = "bigint";
		}else if(Timestamp.class == type){
			value = "timestamp";
		}
		if (value != null) {
			if (f.getAnnotation(PrimaryKey.class) != null) {
				value = value + " " + "not null";	
			}
		}
		return value;
	}
}