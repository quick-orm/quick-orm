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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

public class ConnectionHolder {

	private Map<DataSource, Connection> connectionMap = new HashMap<DataSource, Connection>();

	public Connection getConnection(DataSource dataSource) throws SQLException {
		return connectionMap.get(dataSource);
//		Connection connection = connectionMap.get(dataSource);
//		if (connection == null || connection.isClosed()) {
//			connection = dataSource.getConnection();
//			connectionMap.put(dataSource, connection);
//		}

//		return connection;
	}
	public void putConnection(DataSource dataSource,Connection conn) {
		connectionMap.put(dataSource, conn);
	}

	public void removeConnection(DataSource dataSource) {
		connectionMap.remove(dataSource);
	}
	public void removeConnection(Connection conn) {
		DataSource dataSource = null;
		for (Entry<DataSource, Connection> entry : connectionMap.entrySet()) {
			if (entry.getValue().equals(conn)) {
				dataSource = entry.getKey();
				break;
			}
		}
		connectionMap.remove(dataSource);
	}
}