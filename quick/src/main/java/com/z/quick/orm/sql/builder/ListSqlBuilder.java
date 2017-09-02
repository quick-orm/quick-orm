package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.sql.SqlInfo;
/**
 * ******************  类说明  *********************
 * class       :  GetSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成get SQL
 * @see        :                        
 * ***********************************************
 */
public class ListSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o,valueList);
		String orderBy = super.getOrderBy(o);
		String sql = LIST_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#condition", condition)+orderBy;
		return new SqlInfo(sql, valueList);
	}
	
}




