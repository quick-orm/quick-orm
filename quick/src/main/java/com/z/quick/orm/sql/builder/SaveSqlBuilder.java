package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.sql.SqlInfo;
/**
 * class       :  SaveSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成insert SQL
 * @see        :  *
 */
public class SaveSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		StringBuffer insertParam = new StringBuffer();
		StringBuffer insertValue = new StringBuffer();
		List<Object> valueList = new ArrayList<>();
		super.getInsert(o, insertParam, insertValue, valueList);	
		
		insertParam.deleteCharAt(insertParam.lastIndexOf(","));
		insertValue.deleteCharAt(insertValue.lastIndexOf(","));

		String sql = SAVE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#insertParam", insertParam);
		sql = sql.replace("#insertValue", insertValue);
		return new SqlInfo(sql, valueList);
	}
}
