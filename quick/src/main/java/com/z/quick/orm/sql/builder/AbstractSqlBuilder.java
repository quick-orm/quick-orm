package com.z.quick.orm.sql.builder;

import java.util.List;

import com.z.quick.orm.exception.SqlBuilderException;
import com.z.quick.orm.model.Schema;
import com.z.quick.orm.util.AnnotationSqlBuilderUtils;
import com.z.quick.orm.util.ObjectSqlBuilderUtils;

public abstract class AbstractSqlBuilder implements SqlBuilder {
	protected static final String SAVE_TEMPLATE = "insert into #tableName(#insertParam) values(#insertValue)";
	protected static final String DELETE_TEMPLATE = "delete from #tableName #condition";
	protected static final String UPDATE_TEMPLATE = "update #tableName set #modif #condition";
	protected static final String GET_TEMPLATE = "select #select from #tableName #condition";
	protected static final String LIST_TEMPLATE = "select #select from #tableName #condition";
	protected static final String PAGE_COUNT_TEMPLATE = "select count(1) from #tableName #condition";
	
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
	protected String getPrimaryKey(Object o,List<Object> valueList){
		String condition = ObjectSqlBuilderUtils.getPrimaryKey(o, valueList);
		if (condition != null) {
			return condition;
		}
		condition = AnnotationSqlBuilderUtils.getPrimaryKey(o, valueList);
		return condition;
	}
	protected String getModif(Object o,List<Object> valueList){
		
		String modif = ObjectSqlBuilderUtils.getModif(o, valueList);
		if (modif != null) {
			return modif.substring(0,modif.lastIndexOf(","));
		}
		modif = AnnotationSqlBuilderUtils.getModif(o, valueList);
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






