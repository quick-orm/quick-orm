package com.z.quick.orm.sql.builder;

import java.util.HashMap;
import java.util.Map;

import com.z.quick.orm.sql.SqlInfo;

public class SqlBuilderProcessor {
	private static final Map<SqlBuilder.SBType,SqlBuilder> sqlBuilderContainer = new HashMap<SqlBuilder.SBType,SqlBuilder>();
	
	static{
		sqlBuilderContainer.put(SqlBuilder.SBType.SAVE, new SaveSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.DELETE, new DeleteSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.UPDATE, new UpdateSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.GET, new GetSqlBuilder());
		sqlBuilderContainer.put(SqlBuilder.SBType.LIST, new ListSqlBuilder());
		String dbType = "MySQL";
		if("com.ibm.db2.jcc.DB2Driver".equals(dbType)){
			//sqlBuilderContainer.put(ISqlBuilder.SBType.PAGE_COUNT, new DB2PageSqlBuilder());
			//sqlBuilderContainer.put(ISqlBuilder.SBType.PAGE_LIST, new DB2PageSqlBuilder());
		}else if("com.ibm.db2.jcc.MySQLDriver".equals(dbType)){
			//sqlBuilderContainer.put(ISqlBuilder.SBType.PAGE_COUNT, new MySQLPageSqlBuilder());
			//sqlBuilderContainer.put(ISqlBuilder.SBType.PAGE_LIST, new MySQLPageSqlBuilder());
		}
		
	}
	
	//TODO 从配置文件加载
	
	public static void registerSqlBuilder(SqlBuilder.SBType sBType,SqlBuilder sqlBuilder) {
		sqlBuilderContainer.put(sBType, sqlBuilder);
	}
	
	public static SqlInfo getSql(SqlBuilder.SBType sBType,Object o){
		return sqlBuilderContainer.get(sBType).builderSql(o);
	}
	
	
}