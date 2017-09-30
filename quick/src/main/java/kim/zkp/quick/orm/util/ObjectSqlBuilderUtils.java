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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import kim.zkp.quick.orm.cache.ClassCache;
import kim.zkp.quick.orm.common.Constants;
import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.model.ConditionConstants;
import kim.zkp.quick.orm.model.Model;
import kim.zkp.quick.orm.model.Schema;
import kim.zkp.quick.orm.sharding.Sharding;
import kim.zkp.quick.orm.sql.convert.FieldConvertProcessor;

@SuppressWarnings("unchecked")
public class ObjectSqlBuilderUtils {

	private static final List<String> CONDITION_PARAM = new ArrayList<>(Arrays.asList("lt", "gt", "le", "ge", "eq", "neq", "like"));

	public static String getTableName(Object o) {
		if (o instanceof Sharding) {
			Method m = ClassCache.getStrategyMethod(o.getClass());
			try {
				Object tableName = m.invoke(o);
				if (tableName == null) {
					throw new SqlBuilderException("Strategy is wrong，Ungenerated tableName.");
				}
				return (String) tableName;
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new SqlBuilderException("Strategy is wrong，Ungenerated tableName.",e);
			}
		}
		
		if (o instanceof Model) {
			Field f = ClassCache.getField(o.getClass(), "tableName");
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v != null) {
				return (String) v;
			}
		}
		return null;
	}

	public static String getSelect(Object o) {
		if (o instanceof Model) {
			try {
				Class<?> clzz = o.getClass();
				Field f = ClassCache.getField(clzz, "select");
				Object v = FieldConvertProcessor.toDB(f, o);
				if (v != null) {
					return (String) v;
				}
			} catch (Exception e) {
				throw new SqlBuilderException("Get select list error", e);
			}
		}
		if (o instanceof Schema) {
			return "*";
		}
		return null;
	}

	public static String getCondition(Object o, List<Object> valueList) {
		Class<?> clzz = o.getClass();
		StringBuffer condition = new StringBuffer();
		if (o instanceof Model) {
			CONDITION_PARAM.forEach(s -> {
				Field f = ClassCache.getField(clzz, s);
				assemblyCondition(o, f, condition, valueList);
			});
		}
		Field where = ClassCache.getField(clzz, "where");
		Object v = FieldConvertProcessor.toDB(where, o);
		if (v != null) {
			if (condition.length() == 0) {
				condition.append("where").append(Constants.SPACE).append(v);
			} else {
				condition.append(Constants.SPACE).append("and").append(Constants.SPACE).append(v);
			}
		}
		return condition.length() == 0 ? null : condition.toString();
	}
	public static String getPrimaryKey(Object o, List<Object> valueList) {
		Class<?> clzz = o.getClass();
		StringBuffer condition = new StringBuffer();
		if (o instanceof Model) {
			Field pk = ClassCache.getField(clzz, "pk");
			Object v = FieldConvertProcessor.toDB(pk, o);
			if (v != null) {
				Map<String, Object> pkMap = (Map<String, Object>) v;
				if (pkMap.size() > 0) {
					pkMap.forEach((k, pkv) -> {
						if (condition.length() == 0) {
							condition.append("where").append(Constants.SPACE).append(k).append("=")
									.append(Constants.PLACEHOLDER);
						} else {
							condition.append(Constants.SPACE).append("and").append(Constants.SPACE).append(k)
									.append("=").append(Constants.PLACEHOLDER);
						}
						valueList.add(pkv);
					});
				}
			}
		}
		return condition.length() == 0 ? null : condition.toString();
	}

	public static String getOrderBy(Object o) {
		Class<?> clzz = o.getClass();
		if (o instanceof Model) {
			StringBuffer orderBy = new StringBuffer(Constants.ORDERBY);
			Field f = ClassCache.getField(clzz, "orderByAsc");
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v != null) {
				List<String> orderByAsc = (List<String>) v;
				orderByAsc.forEach(asc -> {
					orderBy.append(asc).append(Constants.SPACE).append("asc").append(",");
				});
			}
			f = ClassCache.getField(clzz, "orderByDesc");
			v = FieldConvertProcessor.toDB(f, o);
			if (v != null) {
				List<String> orderByDesc = (List<String>) v;
				orderByDesc.forEach(asc -> {
					orderBy.append(asc).append(Constants.SPACE).append("desc").append(",");
				});
			}
			if (Constants.ORDERBY.equals(orderBy.toString())) {
				return null;
			}
			return orderBy.deleteCharAt(orderBy.lastIndexOf(",")).toString();
		}
		return null;
	}

	public static String getModif(Object o, List<Object> valueList) {
		if (o instanceof Model) {
			Class<?> clzz = o.getClass();
			StringBuffer modif = new StringBuffer();
			Field f = ClassCache.getField(clzz, "modif");
			Object param = FieldConvertProcessor.toDB(f, o);
			if (param != null) {
				Map<String, Object> map = (Map<String, Object>) param;
				if (map.size() > 0) {
					map.forEach((k, pkv) -> {
						modif.append(k).append("=").append(Constants.PLACEHOLDER).append(",");
						valueList.add(pkv);
					});
				}
			}
			return modif.length() == 0 ? null : modif.toString();
		}
		return null;
	}

	public static void getInsert(Object o, StringBuffer insertParam, StringBuffer insertValue, List<Object> valueList) {
		if (o instanceof Model) {
			try {
				Class<?> clzz = o.getClass();
				Field f = ClassCache.getField(clzz, "insert");
				Object param = FieldConvertProcessor.toDB(f, o);
				if (param != null) {
					Map<String, Object> map = (Map<String, Object>) param;
					if (map.size() > 0) {
						map.forEach((k, pkv) -> {
							insertParam.append(Constants.SPACE).append(k).append(",");
							insertValue.append(Constants.PLACEHOLDER).append(",");
							valueList.add(pkv);
						});
					}
				}
			} catch (Exception e) {
				throw new SqlBuilderException("Get insert param error", e);
			}
		}
	}

	private static void assemblyCondition(Object o, Field f, StringBuffer condition, List<Object> valueList) {
		if (o instanceof Model) {
			try {
				Object v = FieldConvertProcessor.toDB(f, o);
				String logicOperation = ConditionConstants.LOGIC_OPERATION.get(f.getName());
				if (v != null) {
					Map<String, Object> comditionMap = (Map<String, Object>) v;
					if (comditionMap.size() > 0) {
						comditionMap.forEach((k, pkv) -> {
							if (condition.length() == 0) {
								condition.append("where").append(Constants.SPACE).append(k).append(logicOperation)
										.append(Constants.PLACEHOLDER);
							} else {
								condition.append(Constants.SPACE).append("and").append(Constants.SPACE).append(k)
										.append(logicOperation).append(Constants.PLACEHOLDER);
							}
							valueList.add(pkv);
						});
					}
				}
			} catch (Exception e) {
				throw new SqlBuilderException("Get condition param error", e);
			}
		}
	}

}