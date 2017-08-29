package com.z.quick.orm.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

public class ConnectionHolder {

	private Map<DataSource, Connection> connectionMap = new HashMap<DataSource, Connection>();

	public Connection getConnection(DataSource dataSource) throws SQLException {
		Connection connection = connectionMap.get(dataSource);
		if (connection == null || connection.isClosed()) {
			connection = dataSource.getConnection();
			connectionMap.put(dataSource, connection);
		}

		return connection;
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
