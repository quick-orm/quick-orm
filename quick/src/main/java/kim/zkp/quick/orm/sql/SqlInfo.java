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

package kim.zkp.quick.orm.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlInfo {
	
	private String sql;
	private List<Object> param;
	
	public SqlInfo(String sql, List<Object> param) {
		super();
		this.sql = sql;
		if (param == null) {
			this.param = new ArrayList<>();
		}else {
			this.param = param;
		}
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getParam() {
		return param;
	}
	public void setParam(List<Object> param) {
		this.param = param;
	}

	@Override
	public String toString() {
		return "Sql [sql=" + sql + " param=" + param + "]";
	}
	
	
	
}