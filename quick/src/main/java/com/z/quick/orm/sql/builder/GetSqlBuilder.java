package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.sql.SqlInfo;
/**
 * class       :  GetSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成get SQL
 * @see        :  *
 */
public class GetSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o, valueList);
		String sql = GET_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}




