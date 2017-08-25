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
	private static final String template = "SELECT #select FROM #tableName #condition;";

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o,valueList);
		String sql = template.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
	
}




