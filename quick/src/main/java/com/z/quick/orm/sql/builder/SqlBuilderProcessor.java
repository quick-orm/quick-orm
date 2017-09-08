package com.z.quick.orm.sql.builder;

import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.util.JdbcUtils;

public class SqlBuilderProcessor {
	private static final Log log = LogFactory.get();
	private Map<SqlBuilder.SBType,SqlBuilder> sqlBuilderContainer = new HashMap<SqlBuilder.SBType,SqlBuilder>();
	
	public SqlBuilderProcessor(String dbType){
		sqlBuilderContainer.put(SqlBuilder.SBType.SAVE, new SaveSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.DELETE, new DeleteSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.UPDATE, new UpdateSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.GET, new GetSqlBuilder());
		ListSqlBuilder listSqlBuilder = new ListSqlBuilder();
		sqlBuilderContainer.put(SqlBuilder.SBType.LIST, listSqlBuilder);
		sqlBuilderContainer.put(SqlBuilder.SBType.PAGE_COUNT, new PageCountSqlBuilder());
		if(JdbcUtils.MYSQL.equals(dbType)){
			sqlBuilderContainer.put(SqlBuilder.SBType.PAGE_LIST, new MySqlPageListSqlBuilder(listSqlBuilder));
		}else if(JdbcUtils.DB2.equals(dbType)){
			sqlBuilderContainer.put(SqlBuilder.SBType.PAGE_LIST, new DB2PageListSqlBuilder(listSqlBuilder));
		}else if(JdbcUtils.SQLITE.equals(dbType)){
			sqlBuilderContainer.put(SqlBuilder.SBType.PAGE_LIST, new SQLitePageListSqlBuilder());
		}else{
			log.info("No {} db pagelist sql builder",dbType);
		}
		sqlBuilderContainer.put(SqlBuilder.SBType.CREATE_TABLE, new DefaultCreateTableSqlBuilder());
		
	}
	
	public SqlInfo getSql(SqlBuilder.SBType sBType,Object o){
		return sqlBuilderContainer.get(sBType).builderSql(o);
	}
	
}