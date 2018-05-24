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

package kim.zkp.quick.orm.util;

import java.lang.reflect.Field;
import java.util.List;

import kim.zkp.quick.orm.annotation.Condition;
import kim.zkp.quick.orm.annotation.Find;
import kim.zkp.quick.orm.annotation.Join;
import kim.zkp.quick.orm.annotation.OrderBy;
import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.common.Constants;
import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.model.Schema;
import kim.zkp.quick.orm.sql.convert.FieldConvertProcessor;

public class AnnotationSqlBuilderUtils {
	public static String getTableName(Object o) {
		return ClassCache.getTableName(o.getClass());
	}
	public static String getSelect(Object o) {
		if (o instanceof Schema) {
			return " * ";
		}
		return ClassCache.getSelect(o.getClass(),true);
	}
	public static void getCondition(Object o, StringBuilder condition,List<Object> valueList) {
		String alias = ClassCache.getAliasPoint(o.getClass());
		List<Field> fieldList = ClassCache.getAllFieldByCache(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			String operation = Constants.EQUAL;
			String columnName = f.getName();
			Condition conditionAnnotation = f.getAnnotation(Condition.class);
			if (conditionAnnotation != null) {
				operation = conditionAnnotation.value().getOperation();
				if (!"".equals(conditionAnnotation.columnName())) {
					columnName = conditionAnnotation.columnName();
				}
			}
			if (condition.length() == 0) {
				condition.append(Constants.WHERE).append(alias).append(columnName).append(operation).append(Constants.PLACEHOLDER);
			} else {
				condition.append(Constants.AND).append(alias).append(columnName)
				.append(operation).append(Constants.PLACEHOLDER);
			}
			valueList.add(v);
		});
	}
	public static String getPrimaryKey(Object o, List<Object> valueList) {
		StringBuilder condition = new StringBuilder();
		List<Field> fieldList =ClassCache.getPrimaryKey(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			if (condition.length() == 0) {
				condition.append(Constants.WHERE).append(f.getName()).append(Constants.EQUAL).append(Constants.PLACEHOLDER);
			} else {
				condition.append(Constants.AND).append(f.getName())
				.append(Constants.EQUAL).append(Constants.PLACEHOLDER);
			}
			valueList.add(v);
		});
		return condition.toString();
	}
	public static String getOrderBy(Object o) {
		List<Field> fieldList =ClassCache.getOrderBy(o.getClass());
		if (fieldList.size()==0) {
			return "";
		}
		String alias = ClassCache.getAliasPoint(o.getClass());
		StringBuilder orderBy = new StringBuilder(Constants.ORDERBY);
		fieldList.forEach((f) -> {
			String sort = f.getAnnotation(OrderBy.class).value().toString();
			orderBy.append(alias).append(f.getName()).append(Constants.SPACE).append(sort).append(Constants.COMMA);
		});
		return orderBy.deleteCharAt(orderBy.lastIndexOf(Constants.COMMA)).toString();
	}
	
	public static List<Field> getInsert(Object o,StringBuilder insertParam,StringBuilder insertValue,List<Object> valueList) {
		List<Field> fieldList =ClassCache.getInsert(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			insertParam.append(Constants.SPACE).append(f.getName()).append(Constants.COMMA);
			insertValue.append(Constants.PLACEHOLDER).append(Constants.COMMA);
			valueList.add(v);
		});
		return fieldList;
	}
	public static void getModif(Object o,StringBuilder modif,List<Object> valueList) {
		List<Field> fieldList =ClassCache.getInsert(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			modif.append(f.getName()).append(Constants.EQUAL).append(Constants.PLACEHOLDER).append(Constants.COMMA);
			valueList.add(v);
		});
	}
	public static String getJoin(Object o) {
		List<Field> joinList = ClassCache.getJoin(o.getClass());
		StringBuilder joinsb = new StringBuilder();
		String mainAlias = ClassCache.getAliasPoint(o.getClass());
		for (Field f : joinList) {
			joinsb.append(Constants.SPACE).append(Constants.LEFT_JOIN);
			Class<?> joinClass = f.getType();
			String joinTable = ClassCache.getTableName(joinClass);
			
			joinsb.append(joinTable).append(Constants.AS).append(ClassCache.getAlias(joinClass));
			String joinAlias = ClassCache.getAliasPoint(joinClass);
			
			Join join = f.getAnnotation(Join.class);
			String foreignKey = join.foreignKey();
			
			String joinPk = join.joinPk();
			if ("".equals(joinPk)) {
				List<Field> pkList = ClassCache.getPrimaryKey(f.getType());
				if (pkList.size() == 0) {
					throw new SqlBuilderException("Is not specified joinPk");
				}else if (pkList.size() > 1){
					throw new SqlBuilderException("Combined the primary key can't use join find");
				}
				joinPk = pkList.get(0).getName();
			}
			
			joinsb.append(Constants.ON).append(mainAlias).append(foreignKey);
			joinsb.append(Constants.EQUAL).append(joinAlias).append(joinPk);
		}
		
		List<Field> findList = ClassCache.getFind(o.getClass());
		for (Field field : findList) {
			Find find = field.getAnnotation(Find.class);
			String joinAlias = find.joinTable()+Constants.UNDERLINE;
			joinsb.append(Constants.SPACE).append(Constants.LEFT_JOIN);
			joinsb.append(find.joinTable()).append(Constants.AS).append(joinAlias);
			joinsb.append(Constants.ON).append(mainAlias).append(find.foreignKey());
			joinsb.append(Constants.EQUAL).append(joinAlias).append(Constants.POINT).append(find.joinPk());
		}
		
		return joinsb.toString();
	}
}








