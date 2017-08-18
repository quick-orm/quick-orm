package com.z.quick.orm.sql.builder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.z.quick.orm.annotation.Condition;
import com.z.quick.orm.annotation.NoFind;
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
public class FindSqlBuilder extends AbstractSqlBuilder {
	private static final String template = "SELECT #selectParam FROM #tableName #whereParam;";

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
			if (f.getAnnotation(Condition.class) != null) {
				Object v = super.getValue(f, o);
				if (v == null) {
					return ;
				}
				if (whereParam.length() == 0 ) {
					whereParam.append("WHERE").append(space).append(f.getName()).append("=").append(placeholder);
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




