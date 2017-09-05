package com.z.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.exception.SqlBuilderException;
import com.z.quick.orm.sql.SqlInfo;
/**
 * class       :  UpdateSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成update sql
 * @see        :  *
 */
public class UpdateSqlBuilder extends AbstractSqlBuilder {
	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String modif = super.getModif(o, valueList);
		String condition = super.getPrimaryKey(o,valueList);
		if (condition == null || "".equals(condition)) {
			throw new SqlBuilderException("No update condition,disallow full table update!");
		}
		String sql = UPDATE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#modif", modif);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}




