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

import javax.sql.DataSource;

public class SingleThreadConnectionHolder {
	private static ThreadLocal<ConnectionHolder> localConnectionHolder = new ThreadLocal<ConnectionHolder>();

	public static Connection getConnection(DataSource dataSource) throws SQLException {
		return getConnectionHolder().getConnection(dataSource);
	}
	public static void putConnection(DataSource dataSource,Connection conn) throws SQLException {
		getConnectionHolder().putConnection(dataSource,conn);
	}

	public static void removeConnection(DataSource dataSource) {
		ConnectionHolder connectionHolder = localConnectionHolder.get();
		if (connectionHolder != null) {
			connectionHolder.removeConnection(dataSource);
		}
	}
	public static void removeConnection(Connection conn) {
		getConnectionHolder().removeConnection(conn);
	}

	private static ConnectionHolder getConnectionHolder() {
		ConnectionHolder connectionHolder = localConnectionHolder.get();
		if (connectionHolder == null) {
			connectionHolder = new ConnectionHolder();
			localConnectionHolder.set(connectionHolder);
		}
		return connectionHolder;
	}
}