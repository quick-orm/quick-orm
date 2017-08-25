package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.exception.SqlBuilderException;
import com.z.quick.orm.sql.SqlInfo;
/**
 * class       :  DeleteSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成delete sql
 * @see        :  *
 */
public class DeleteSqlBuilder extends AbstractSqlBuilder {
	private static final String template = "DELETE FROM #tableName #condition;";

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o, valueList);
		if (condition == null || "".equals(condition)) {
			throw new SqlBuilderException("disallow full table delete");
		}
		String sql = template.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}




