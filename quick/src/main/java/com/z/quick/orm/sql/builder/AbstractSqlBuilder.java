package com.z.quick.orm.sql.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.z.quick.orm.annotation.Exclude;
import com.z.quick.orm.exception.SqlBuilderException;
import com.z.quick.orm.oop.Schema;
import com.z.quick.orm.util.AnnotationSqlBuilderUtils;
import com.z.quick.orm.util.ObjectSqlBuilderUtils;

public abstract class AbstractSqlBuilder implements ISqlBuilder {
	//TODO 删除
	protected static final String space = " ";
	protected static final String placeholder = "?";
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
	protected void getInsert(Object o,StringBuffer insertParam,StringBuffer insertValue,List<Object> valueList){
		ObjectSqlBuilderUtils.getInsert(o, insertParam, insertValue, valueList);
		if (valueList.size()>0) {
			return ;
		}
		AnnotationSqlBuilderUtils.getInsert(o, insertParam, insertValue, valueList);;
		if (valueList.size()<=0) {
			throw new SqlBuilderException("Insert param is null");
		}
	}
	
	protected String getCondition(Object o,List<Object> valueList){
		
		String condition = ObjectSqlBuilderUtils.getCondition(o, valueList);
		if (condition != null) {
			return condition;
		}
		condition = AnnotationSqlBuilderUtils.getCondition(o, valueList);
		return condition;
	}
	protected String getModif(Object o,List<Object> valueList){
		
		String modif = ObjectSqlBuilderUtils.getModif(o, valueList);
		if (modif != null) {
			return modif.substring(0,modif.lastIndexOf(","));
		}
		modif = AnnotationSqlBuilderUtils.getModif(o, valueList);
		if (modif.length()<=0) {
			throw new SqlBuilderException("modif param is null");
		}
		return modif.substring(0,modif.lastIndexOf(","));
	}
	
	/**
	 * ********************************************
	 * method name   : getFields 
	 * description   : 获取需要生成sql的属性
	 * @return       : List<Field>
	 * @param        : @param clzz
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年8月14日  下午2:38:28
	 * @see          : 
	 * *******************************************
	 */
	protected List<Field> getFields(Class<?> clzz){
		Field[] fields =  clzz.getDeclaredFields();
		List<Field> fieldList = new ArrayList<Field>(Arrays.asList(fields));
		fieldList.removeIf(f -> {
			//f.setAccessible(true);
			Exclude e = f.getAnnotation(Exclude.class);
			return e != null;
		}); 
		return fieldList;
	}
}






