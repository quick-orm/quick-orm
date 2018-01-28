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

import kim.zkp.quick.orm.exception.SqlBuilderException;
import kim.zkp.quick.orm.sql.SqlInfo;
/**
 * class       :  UpdateSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成update sql
 * @see        :  *
 */
public class UpdateSqlBuilder extends AbstractSqlBuilder {
	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		List<Object> valueList = new ArrayList<>();
		String modif = super.getModif(o, valueList);
		String condition = super.getPrimaryKey(o,valueList);
		if (condition == null || "".equals(condition)) {
			throw new SqlBuilderException("No update condition,disallow full table update!");
		}
		String sql = UPDATE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#modif", modif);
		sql = sql.replace("#condition", condition);
		return new SqlInfo(sql, valueList);
	}
}