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

package kim.zkp.quick.orm.util;

import java.sql.SQLException;

public final class JdbcUtils {
	public static final String JTDS = "jtds";
	public static final String MOCK = "mock";
	public static final String HSQL = "hsql";
	public static final String DB2 = "db2";
	public static final String SQLITE = "sqlite";
	public static final String DB2_DRIVER = "COM.ibm.db2.jdbc.app.DB2Driver";
	public static final String POSTGRESQL = "postgresql";
	public static final String SYBASE = "sybase";
	public static final String SQL_SERVER = "sqlserver";
	public static final String ORACLE = "oracle";
	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String ALI_ORACLE = "AliOracle";
	public static final String ALI_ORACLE_DRIVER = "com.alibaba.jdbc.AlibabaDriver";
	public static final String MYSQL = "mysql";
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String MARIADB = "mariadb";
	public static final String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";
	public static final String DERBY = "derby";
	public static final String HBASE = "hbase";
	public static final String HIVE = "hive";
	public static final String H2 = "h2";
	public static final String H2_DRIVER = "org.h2.Driver";

	public static String getDriverClassName(String rawUrl) throws SQLException {
		if (rawUrl.startsWith("jdbc:derby:")) {
			return "org.apache.derby.jdbc.EmbeddedDriver";
		} else if (rawUrl.startsWith("jdbc:mysql:")) {
			return MYSQL_DRIVER;
		} else if (rawUrl.startsWith("jdbc:mariadb:")) {
			return MARIADB_DRIVER;
		} else if (rawUrl.startsWith("jdbc:oracle:") //
				|| rawUrl.startsWith("JDBC:oracle:")) {
			return ORACLE_DRIVER;
		} else if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
			return ALI_ORACLE_DRIVER;
		} else if (rawUrl.startsWith("jdbc:microsoft:")) {
			return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		} else if (rawUrl.startsWith("jdbc:sqlserver:")) {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else if (rawUrl.startsWith("jdbc:sybase:Tds:")) {
			return "com.sybase.jdbc2.jdbc.SybDriver";
		} else if (rawUrl.startsWith("jdbc:jtds:")) {
			return "net.sourceforge.jtds.jdbc.Driver";
		} else if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
			return "com.alibaba.druid.mock.MockDriver";
		} else if (rawUrl.startsWith("jdbc:postgresql:")) {
			return "org.postgresql.Driver";
		} else if (rawUrl.startsWith("jdbc:hsqldb:")) {
			return "org.hsqldb.jdbcDriver";
		} else if (rawUrl.startsWith("jdbc:db2:")) {
			return DB2_DRIVER;
		} else if (rawUrl.startsWith("jdbc:sqlite:")) {
			return "org.sqlite.JDBC";
		} else if (rawUrl.startsWith("jdbc:ingres:")) {
			return "com.ingres.jdbc.IngresDriver";
		} else if (rawUrl.startsWith("jdbc:h2:")) {
			return H2_DRIVER;
		} else if (rawUrl.startsWith("jdbc:mckoi:")) {
			return "com.mckoi.JDBCDriver";
		} else if (rawUrl.startsWith("jdbc:cloudscape:")) {
			return "COM.cloudscape.core.JDBCDriver";
		} else if (rawUrl.startsWith("jdbc:informix-sqli:")) {
			return "com.informix.jdbc.IfxDriver";
		} else if (rawUrl.startsWith("jdbc:timesten:")) {
			return "com.timesten.jdbc.TimesTenDriver";
		} else if (rawUrl.startsWith("jdbc:as400:")) {
			return "com.ibm.as400.access.AS400JDBCDriver";
		} else if (rawUrl.startsWith("jdbc:sapdb:")) {
			return "com.sap.dbtech.jdbc.DriverSapDB";
		} else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
			return "com.jnetdirect.jsql.JSQLDriver";
		} else if (rawUrl.startsWith("jdbc:JTurbo:")) {
			return "com.newatlanta.jturbo.driver.Driver";
		} else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
			return "org.firebirdsql.jdbc.FBDriver";
		} else if (rawUrl.startsWith("jdbc:interbase:")) {
			return "interbase.interclient.Driver";
		} else if (rawUrl.startsWith("jdbc:pointbase:")) {
			return "com.pointbase.jdbc.jdbcUniversalDriver";
		} else if (rawUrl.startsWith("jdbc:edbc:")) {
			return "ca.edbc.jdbc.EdbcDriver";
		} else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
			return "com.mimer.jdbc.Driver";
		} else {
			throw new SQLException("unkow jdbc driver : " + rawUrl);
		}
	}

	public static String getDbType(String rawUrl) {
		if (rawUrl == null) {
			return null;
		}

		if (rawUrl.startsWith("jdbc:derby:")) {
			return DERBY;
		} else if (rawUrl.startsWith("jdbc:mysql:")) {
			return MYSQL;
		} else if (rawUrl.startsWith("jdbc:mariadb:")) {
			return MARIADB;
		} else if (rawUrl.startsWith("jdbc:oracle:")) {
			return ORACLE;
		} else if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
			return ALI_ORACLE;
		} else if (rawUrl.startsWith("jdbc:microsoft:")) {
			return SQL_SERVER;
		} else if (rawUrl.startsWith("jdbc:sybase:Tds:")) {
			return SYBASE;
		} else if (rawUrl.startsWith("jdbc:jtds:")) {
			return JTDS;
		} else if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
			return MOCK;
		} else if (rawUrl.startsWith("jdbc:postgresql:")) {
			return POSTGRESQL;
		} else if (rawUrl.startsWith("jdbc:hsqldb:")) {
			return HSQL;
		} else if (rawUrl.startsWith("jdbc:db2:")) {
			return DB2;
		} else if (rawUrl.startsWith("jdbc:sqlite:")) {
			return SQLITE;
		} else if (rawUrl.startsWith("jdbc:ingres:")) {
			return "ingres";
		} else if (rawUrl.startsWith("jdbc:h2:")) {
			return H2;
		} else if (rawUrl.startsWith("jdbc:mckoi:")) {
			return "mckoi";
		} else if (rawUrl.startsWith("jdbc:cloudscape:")) {
			return "cloudscape";
		} else if (rawUrl.startsWith("jdbc:informix-sqli:")) {
			return "informix";
		} else if (rawUrl.startsWith("jdbc:timesten:")) {
			return "timesten";
		} else if (rawUrl.startsWith("jdbc:as400:")) {
			return "as400";
		} else if (rawUrl.startsWith("jdbc:sapdb:")) {
			return "sapdb";
		} else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
			return "JSQLConnect";
		} else if (rawUrl.startsWith("jdbc:JTurbo:")) {
			return "JTurbo";
		} else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
			return "firebirdsql";
		} else if (rawUrl.startsWith("jdbc:interbase:")) {
			return "interbase";
		} else if (rawUrl.startsWith("jdbc:pointbase:")) {
			return "pointbase";
		} else if (rawUrl.startsWith("jdbc:edbc:")) {
			return "edbc";
		} else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
			return "mimer";
		} else {
			return null;
		}
	}

}