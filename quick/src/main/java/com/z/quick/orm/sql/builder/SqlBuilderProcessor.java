package com.z.quick.orm.sql.builder;

import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.z.quick.orm.session.Session;
import com.z.quick.orm.sql.SqlInfo;
import com.z.quick.orm.util.JdbcUtils;

public class SqlBuilderProcessor {
	private static final Log log = LogFactory.get();
	private static final Map<SqlBuilder.SBType,SqlBuilder> sqlBuilderContainer = new HashMap<SqlBuilder.SBType,SqlBuilder>();
	static{
		registerSqlBuilder(SqlBuilder.SBType.SAVE, new SaveSqlBuilder());
		registerSqlBuilder(SqlBuilder.SBType.DELETE, new DeleteSqlBuilder());
		registerSqlBuilder(SqlBuilder.SBType.UPDATE, new UpdateSqlBuilder());
		registerSqlBuilder(SqlBuilder.SBType.GET, new GetSqlBuilder());
		registerSqlBuilder(SqlBuilder.SBType.LIST, new ListSqlBuilder());
		registerSqlBuilder(SqlBuilder.SBType.PAGE_COUNT, new PageCountSqlBuilder());
		if(JdbcUtils.MYSQL.equals(Session.getSession().getJdbcConfig().getDbType())){
			registerSqlBuilder(SqlBuilder.SBType.PAGE_LIST, new MySqlPageListSqlBuilder());
		}else if(JdbcUtils.DB2.equals(Session.getSession().getJdbcConfig().getDbType())){
			registerSqlBuilder(SqlBuilder.SBType.PAGE_LIST, new DB2PageListSqlBuilder());
		}else if(JdbcUtils.SQLITE.equals(Session.getSession().getJdbcConfig().getDbType())){
			registerSqlBuilder(SqlBuilder.SBType.PAGE_LIST, new SQLitePageListSqlBuilder());
		}else{
			log.info("No {} db pagelist sql builder",Session.getSession().getJdbcConfig().getDbType());
		}
		
	}
	
	public static void registerSqlBuilder(SqlBuilder.SBType sBType,SqlBuilder sqlBuilder) {
		sqlBuilderContainer.put(sBType, sqlBuilder);
	}
	
	public static SqlInfo getSql(SqlBuilder.SBType sBType,Object o){
		return sqlBuilderContainer.get(sBType).builderSql(o);
	}
	
	
}