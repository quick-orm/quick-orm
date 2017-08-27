package com.z.quick.orm.util;

import java.lang.reflect.Field;
import java.util.List;

import com.z.quick.orm.annotation.Condition;
import com.z.quick.orm.cache.ClassCache;
import com.z.quick.orm.common.Constants;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

public class AnnotationSqlBuilderUtils {
	public static String getTableName(Object o) {
		return ClassCache.getTableName(o.getClass());
	}
	public static String getSelect(Object o) {
		return ClassCache.getSelect(o.getClass());
	}

	public static String getCondition(Object o, List<Object> valueList) {
		StringBuffer condition = new StringBuffer();
		List<Field> fieldList =ClassCache.getCondition(o.getClass());
		fieldList.forEach((f) -> {
			Object v = FieldConvertProcessor.toDB(f, o);
			if (v == null) {
				return;
			}
			String operation = f.getAnnotation(Condition.class).value().getOperation();
			if (condition.length() == 0) {
				condition.append("WHERE").append(Constants.SPACE).append(f.getName()).append(operation).append(Constants.PLACEHOLDER);
			} else {
				condition.append(Constants.SPACE).append("AND").append(Constants.SPACE).append(f.getName())
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
				condition.append("WHERE").append(Constants.SPACE).append(f.getName()).append("=").append(Constants.PLACEHOLDER);
			} else {
				condition.append(Constants.SPACE).append("AND").append(Constants.SPACE).append(f.getName())
				.append("=").append(Constants.PLACEHOLDER);
			}
			valueList.add(v);
		});
		return condition.toString();
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
