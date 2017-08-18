package com.z.quick.orm.sql.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.z.quick.orm.annotation.Exclude;
import com.z.quick.orm.annotation.Table;
import com.z.quick.orm.sql.convert.FieldConvertProcessor;

public abstract class AbstractSqlBuilder implements ISqlBuilder {
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
	protected String getTableName(Class<?> clzz){
		return clzz.getAnnotation(Table.class).value();
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
			f.setAccessible(true);
			Exclude e = f.getAnnotation(Exclude.class);
			return e != null;
		}); 
		return fieldList;
	}
	/**
	 * ********************************************
	 * method name   : getValue 
	 * description   : 获取转换成数据库类型的属性值
	 * @return       : Object
	 * @param        : @param f
	 * @param        : @param o
	 * @param        : @return
	 * modified      : zhukaipeng ,  2017年8月14日  下午4:42:57
	 * @see          : 
	 * *******************************************
	 */
	protected Object getValue(Field f,Object o){
		Object v = null;
		try {
			v = f.get(o);
			if (v == null) {
				return null;
			}
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("通过反射获取属性值出错!",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("通过反射获取属性值出错!",e);
		}
		return FieldConvertProcessor.toDB(v);
	}
}






