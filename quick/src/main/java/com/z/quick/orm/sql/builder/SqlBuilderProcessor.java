package com.z.quick.orm.sql.builder;

import java.util.HashMap;
import java.util.Map;

import com.z.quick.orm.sql.SqlInfo;

public class SqlBuilderProcessor {
	private static final Map<ISqlBuilder.SBType,ISqlBuilder> sqlBuilderContainer = new HashMap<ISqlBuilder.SBType,ISqlBuilder>();
	
	static{
		sqlBuilderContainer.put(ISqlBuilder.SBType.SAVE, new SaveSqlBuilder());
		sqlBuilderContainer.put(ISqlBuilder.SBType.GET, new GetSqlBuilder());
		sqlBuilderContainer.put(ISqlBuilder.SBType.FIND, new FindSqlBuilder());
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
	
	public static void registerSqlBuilder(ISqlBuilder.SBType sBType,ISqlBuilder sqlBuilder) {
		sqlBuilderContainer.put(sBType, sqlBuilder);
	}
	
	public static SqlInfo getSql(ISqlBuilder.SBType sBType,Object o){
		return sqlBuilderContainer.get(sBType).builderSql(o);
	}
	
	
}