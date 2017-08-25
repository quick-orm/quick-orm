package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.z.quick.orm.sql.SqlInfo;
/**
 * class       :  UpdateSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成update sql
 * @see        :  *
 */
public class UpdateSqlBuilder extends AbstractSqlBuilder {
	private static final String template = "UPDATE #tableName SET #modif #condition;";
	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String modif = super.getModif(o, valueList);
		String condition = super.getCondition(o,valueList);
		String sql = template.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#modif", modif);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}




