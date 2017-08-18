package com.z.quick.orm.sql;

public class DBType {
	   public static final String JTDS              = "jtds";

	    public static final String MOCK              = "mock";

	    public static final String HSQL              = "hsql";

	    public static final String DB2               = "db2";

	    public static final String DB2_DRIVER        = "COM.ibm.db2.jdbc.app.DB2Driver";

	    public static final String POSTGRESQL        = "postgresql";

	    public static final String SYBASE            = "sybase";

	    public static final String SQL_SERVER        = "sqlserver";

	    public static final String ORACLE            = "oracle";
	    public static final String ORACLE_DRIVER     = "oracle.jdbc.driver.OracleDriver";

	    public static final String ALI_ORACLE        = "AliOracle";
	    public static final String ALI_ORACLE_DRIVER = "com.alibaba.jdbc.AlibabaDriver";

	    public static final String MYSQL             = "mysql";
	    public static final String MYSQL_DRIVER      = "com.mysql.jdbc.Driver";

	    public static final String MARIADB           = "mariadb";
	    public static final String MARIADB_DRIVER    = "org.mariadb.jdbc.Driver";

	    public static final String DERBY             = "derby";

	    public static final String HBASE             = "hbase";

	    public static final String HIVE              = "hive";

	    public static final String H2                = "h2";

	    public static final String H2_DRIVER         = "org.h2.Driver";

	    /**
	     * 阿里云odps
	     */
	    public static final String ODPS              = "odps";
	    
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
            return "sqlite";
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
