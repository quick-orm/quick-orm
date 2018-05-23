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

import java.util.ArrayList;
import java.util.List;

import kim.zkp.quick.orm.sql.SqlInfo;
/**
 * class       :  GetSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成get SQL
 * @see        :  *
 */
public class GetSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		String alias = super.getAlias(o);
		String select = super.getSelect(o);
		List<Object> valueList = new ArrayList<>();
		String condition = super.getCondition(o, valueList);
		String join = super.getJoin(o);
		String sql = GET_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#select", select);
		sql = sql.replace("#alias", alias);
		sql = sql.replace("#join", join);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}