/**
 * Copyright (c) 2017, ZhuKaipeng 朱开鹏 (2076528290@qq.com).

 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kim.zkp.quick.orm.sql.builder;

import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import kim.zkp.quick.orm.sql.SqlInfo;
import kim.zkp.quick.orm.util.JdbcUtils;

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