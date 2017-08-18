package com.z.quick.orm.sql.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.sql.SqlInfo;
/**
 * ******************  类说明  *********************
 * class       :  SaveSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成insert SQL
 * @see        :                        
 * ***********************************************
 */
public class SaveSqlBuilder extends AbstractSqlBuilder {
	private static final String template = "INSERT INTO #tableName(#insertParam) VALUES(#insertValue);";

	@Override
	public SqlInfo builderSql(Object o) {
		Class<?> clzz = o.getClass();
		String tableName = super.getTableName(clzz);
		List<Field> fieldList = super.getFields(clzz);

		StringBuffer insertParam = new StringBuffer();
		StringBuffer insertValue = new StringBuffer();
		List<Object> valueList = new ArrayList<>();

		fieldList.forEach((f) -> {
			Object v = super.getValue(f, o);
			if (v == null) {
				return;
			}
			insertParam.append(space).append(f.getName()).append(",");
			insertValue.append(placeholder).append(",");
			valueList.add(v);
		});
		insertParam.deleteCharAt(insertParam.lastIndexOf(","));
		insertValue.deleteCharAt(insertValue.lastIndexOf(","));

		String sql = template.replace("#tableName", tableName);
		sql = sql.replace("#insertParam", insertParam);
		sql = sql.replace("#insertValue", insertValue);
		return new SqlInfo(sql, valueList);
	}
}
