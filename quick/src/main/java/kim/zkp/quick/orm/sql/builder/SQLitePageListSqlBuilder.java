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

import java.util.Map;

import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.model.Page;
import kim.zkp.quick.orm.sql.SqlInfo;
/**
 * ******************  类说明  *********************
 * class       :  GetSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成get SQL
 * @see        :                        
 * ***********************************************
 */
public class SQLitePageListSqlBuilder extends AbstractSqlBuilder {
	private static final String PAGE_LIST_TEMPLATE = "#listSql limit #start,#end";
	private final SqlBuilder listBuilder = new ListSqlBuilder();
	@Override
	public SqlInfo builderSql(Object o) {
		Map<String,Integer> pageInfo = Page.getPageInfo();
		if (pageInfo == null || pageInfo.get("pageNum")==null || pageInfo.get("pageSize")==null) {
			throw new SqlBuilderException("PageNum or pageSize is null");
		}
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