package com.z.quick.orm.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.z.quick.orm.cache.ClassCache;
import com.z.quick.orm.common.Constants;
import com.z.quick.orm.exception.SqlBuilderException;
import com.z.quick.orm.model.LogicConstants;
import com.z.quick.orm.model.Model;
import com.z.quick.orm.model.Schema;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

@SuppressWarnings("unchecked")
public class ObjectSqlBuilderUtils {

	private static final List<String> CONDITION_PARAM = new ArrayList<>(Arrays.asList("lt", "gt", "le", "ge", "eq", "neq"));

	public static String getTableName(Object o) {
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
		if (o instanceof Model) {
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
					condition.append("WHERE").append(Constants.SPACE).append(v);
				} else {
					condition.append(Constants.SPACE).append("AND").append(Constants.SPACE).append(v);
				}
			}
			;
			return condition.length() == 0 ? null : condition.toString();
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
				String logicOperation = LogicConstants.LOGIC_OPERATION.get(f.getName());
				if (v != null) {
					Map<String, Object> pk = (Map<String, Object>) v;
					if (pk.size() > 0) {
						pk.forEach((k, pkv) -> {
							if (condition.length() == 0) {
								condition.append("WHERE").append(Constants.SPACE).append(k).append(logicOperation)
										.append(Constants.PLACEHOLDER);
							} else {
								condition.append(Constants.SPACE).append("AND").append(Constants.SPACE).append(k)
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
