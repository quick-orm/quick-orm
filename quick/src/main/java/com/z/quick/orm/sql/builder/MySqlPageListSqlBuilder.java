package com.z.quick.orm.sql.builder;

import java.util.Map;

import com.z.quick.orm.model.Page;
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
public class MySqlPageListSqlBuilder extends AbstractSqlBuilder {
	private static final String PAGE_LIST_TEMPLATE = "#listSql limit #start,#end";
	private SqlBuilder listBuilder;
	
	public MySqlPageListSqlBuilder(SqlBuilder listBuilder) {
		super();
		this.listBuilder = listBuilder;
	}

	@Override
	public SqlInfo builderSql(Object o) {
		Map<String,Integer> pageInfo = Page.getPageInfo();
		SqlInfo sqlInfo = listBuilder.builderSql(o);
		String sql = sqlInfo.getSql();
		Integer pageNum = pageInfo.get("pageNum");
		Integer pageSize = pageInfo.get("pageSize");
		Integer start = (pageNum-1)*pageSize;
		sql = PAGE_LIST_TEMPLATE.replace("#listSql", sql);
		sql = sql.replace("#start", start.toString());
		sql = sql.replace("#end", pageSize.toString());
		sqlInfo.setSql(sql);
		
		return sqlInfo;
	}
	
}




