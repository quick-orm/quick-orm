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

package kim.zkp.quick.orm.connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.TimeInterval;

import kim.zkp.quick.monitor.MonitorSql;
import kim.zkp.quick.orm.sql.SqlInfo;

/**
 * ****************** 类说明 ********************* class : PreparedStatementWrapper
 * 
 * @author : zhukaipeng
 * @version : 1.0 description : 目前无法从代理类内部获取到执行的sql，无法用代理实现 后期增加sql预编译缓存再更改
 * @see : ***********************************************
 */
public class PreparedStatementWrapper {

	private PreparedStatement stmt;
	private SqlInfo sqlInfo;
	private boolean executeTimeMonitor;
	private long maxExecuteTime;
	
	public PreparedStatementWrapper(PreparedStatement stmt, SqlInfo sqlInfo, boolean executeTimeMonitor,
			long maxExecuteTime) {
		super();
		this.stmt = stmt;
		this.sqlInfo = sqlInfo;
		this.executeTimeMonitor = executeTimeMonitor;
		this.maxExecuteTime = maxExecuteTime;
	}

	public int executeUpdate() throws SQLException {
		if (!executeTimeMonitor)
			return stmt.executeUpdate();

		TimeInterval timer = DateUtil.timer();
		int result = stmt.executeUpdate();
		if (timer.interval() > maxExecuteTime) {
			MonitorSql.record(sqlInfo.getSql(),timer.interval());
		}
		return result;
	}

	public ResultSet executeQuery() throws SQLException {
		if (!executeTimeMonitor)
			return stmt.executeQuery();

		TimeInterval timer = DateUtil.timer();
		ResultSet result = stmt.executeQuery();
		if (timer.interval() > maxExecuteTime) {
			MonitorSql.record(sqlInfo.getSql(),timer.interval());
		}
		return result;
	}

	public void close() throws SQLException {
		stmt.close();
	}

}