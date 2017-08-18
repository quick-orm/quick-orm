package com.z.quick.orm.sql.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.annotation.NoFind;
import com.z.quick.orm.annotation.PrimaryKey;
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
public class GetSqlBuilder extends AbstractSqlBuilder {
	private static final String template = "SELECT #selectParam FROM #tableName WHERE #whereParam;";

	@Override
	public SqlInfo builderSql(Object o) {
		Class<?> clzz = o.getClass();
		String tableName = super.getTableName(clzz);
		List<Field> fieldList = super.getFields(clzz);
		fieldList.removeIf((f) -> f.getAnnotation(NoFind.class)!=null); 
		StringBuffer selectParam = new StringBuffer();
		StringBuffer whereParam = new StringBuffer();
		List<Object> valueList = new ArrayList<>();
		fieldList.forEach((f) -> {
			selectParam.append(space).append(f.getName()).append(",");
			if (f.getAnnotation(PrimaryKey.class) != null) {
				Object v = super.getValue(f, o);
				if (v == null) {
					throw new RuntimeException("生成查询sql出错，主键为空!");
				}
				if (whereParam.length() == 0 ) {
					whereParam.append(f.getName()).append("=").append(placeholder);
				}else {
					whereParam.append(space).append("AND").append(space).append(f.getName()).append("=").append(placeholder);
				}
				valueList.add(v);
			}
		});
		selectParam.deleteCharAt(selectParam.lastIndexOf(","));

		String sql = template.replace("#tableName", tableName);
		sql = sql.replace("#selectParam", selectParam);
		sql = sql.replace("#whereParam", whereParam);
		return new SqlInfo(sql, valueList);
	}
}




