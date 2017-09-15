package kim.zkp.quick.orm.sql.builder;

import java.util.ArrayList;
import java.util.List;

import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.sql.SqlInfo;
/**
 * class       :  DeleteSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成delete sql
 * @see        :  *
 */
public class DeleteSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o, valueList);
		if (condition == null || "".equals(condition)) {
			throw new SqlBuilderException("No delete condition,disallow full table delete!");
		}
		String sql = DELETE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}




