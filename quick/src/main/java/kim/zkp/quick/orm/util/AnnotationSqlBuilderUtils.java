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
import kim.zkp.quick.orm.annotation.OrderBy;
import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.common.Constants;
import kim.zkp.quick.orm.sql.convert.FieldConvertProcessor;

public class AnnotationSqlBuilderUtils {
	public static String getTableName(Object o) {
		return ClassCache.getTableName(o.getClass());
	}
	public static String getSelect(Object o) {
		return ClassCache.getSelect(o.getClass());
	}

	public static String getCondition(Object o, List<Object> valueList) {
		StringBuffer condition = new StringBuffer();
		List<Field> fieldList = ClassCache.getAllDeclaredFields(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			String operation = " = ";
			if ( f.getAnnotation(Condition.class) != null) {
				operation = f.getAnnotation(Condition.class).value().getOperation();
			}
			if (condition.length() == 0) {
				condition.append("where").append(Constants.SPACE).append(f.getName()).append(operation).append(Constants.PLACEHOLDER);
			} else {
				condition.append(Constants.SPACE).append("and").append(Constants.SPACE).append(f.getName())
				.append(operation).append(Constants.PLACEHOLDER);
			}
			valueList.add(v);
		});
		return condition.toString();
	}
	public static String getPrimaryKey(Object o, List<Object> valueList) {
		StringBuffer condition = new StringBuffer();
		List<Field> fieldList =ClassCache.getPrimaryKey(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			if (condition.length() == 0) {
				condition.append("where").append(Constants.SPACE).append(f.getName()).append("=").append(Constants.PLACEHOLDER);
			} else {
				condition.append(Constants.SPACE).append("and").append(Constants.SPACE).append(f.getName())
				.append("=").append(Constants.PLACEHOLDER);
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
		StringBuffer orderBy = new StringBuffer(Constants.ORDERBY);
		fieldList.forEach((f) -> {
			String sort = f.getAnnotation(OrderBy.class).value().toString();
			orderBy.append(f.getName()).append(Constants.SPACE).append(sort).append(",");
		});
		return orderBy.deleteCharAt(orderBy.lastIndexOf(",")).toString();
	}
	
	public static void getInsert(Object o,StringBuffer insertParam,StringBuffer insertValue,List<Object> valueList) {
		List<Field> fieldList =ClassCache.getInsert(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			insertParam.append(Constants.SPACE).append(f.getName()).append(",");
			insertValue.append(Constants.PLACEHOLDER).append(",");
			valueList.add(v);
		});
	}
	public static String getModif(Object o,List<Object> valueList) {
		List<Field> fieldList =ClassCache.getInsert(o.getClass());
		StringBuffer modif = new StringBuffer();
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			modif.append(f.getName()).append("=").append(Constants.PLACEHOLDER).append(",");
			valueList.add(v);
		});
		return modif.toString();
	}
}