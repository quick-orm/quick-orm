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
 * class       :  SaveSqlBuilder
 * @author     :  zhukaipeng
 * @version    :  1.0  
 * description :  生成insert SQL
 * @see        :  *
 */
public class SaveSqlBuilder extends AbstractSqlBuilder {

	@Override
	public SqlInfo builderSql(Object o) {
		String tableName = super.getTableName(o);
		StringBuffer insertParam = new StringBuffer();
		StringBuffer insertValue = new StringBuffer();
		List<Object> valueList = new ArrayList<>();
		super.getInsert(o, insertParam, insertValue, valueList);	
		
		insertParam.deleteCharAt(insertParam.lastIndexOf(","));
		insertValue.deleteCharAt(insertValue.lastIndexOf(","));

		String sql = SAVE_TEMPLATE.replace("#tableName", tableName);
		sql = sql.replace("#insertParam", insertParam);
		sql = sql.replace("#insertValue", insertValue);
		return new SqlInfo(sql, valueList);
	}
}