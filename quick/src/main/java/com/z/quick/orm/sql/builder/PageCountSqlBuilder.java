package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.sql.SqlInfo;
/**
 * class       :  PageCountSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成PageCountSql
 * @see        :  *
 */
public class PageCountSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o, valueList);
		String sql = PAGE_COUNT_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}




