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

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.setting.Setting;

import kim.zkp.quick.orm.exception.ConnectionException;
import kim.zkp.quick.orm.util.JdbcUtils;

public class JDBCConfig implements DefaultConfig {
	private static final Log log = LogFactory.get();
	/**数据库地址*/
	private String url;
	/**数据库用户名*/
	private String username;
	/**数据库密码*/
	private String password;
	/**数据库驱动*/
	private String driverClassName;
	/**数据库类型*/
	private String dbType;
	/**最大连接数*/
	private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
	/**最小连接数*/
	private int minPoolSize = DEFAULT_MIN_POOL_SIZE;
	/**初始化连接数*/
	private int initialPoolSize = DEFAULT_INITIAL_POOL_SIZE;
	/**是否启用sql监控*/
	private boolean executeTimeMonitor = DEFAULT_EXECUTE_TIME_MONITOR;
	/**启用sql监控后，sql最大执行时长，超过该时长的sql会被记录在文件中*/
	private long maxExecuteTime = DEFAULT_MAX_EXECUTE_TIME;
	/**最大耗时sql保存文件路径 默认classes目录下*/
	private String maxExecuteTimeFilePath = DEFAULT_MAX_EXECUTE_TIME_FILE_PATH;
	/**单位毫秒 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出ConnectionException。单位毫秒。默认: 10000*/
	private int maxWaitTime = DEFAULT_MAX_WAIT_TIME;
	/**当连接池连接耗尽时，每次轮询获取连接的间隔时间，默认50毫秒*/
	private int oncePollTime= DEFAULT_ONCE_POLL_TIME;
	/**最大空闲时间*/
	private int maxIdleTime = DEFAULT_MAX_IDLE_TIME;
	/**每X毫秒检查所有连接池中的空闲连接。默认值: 0，不检查*/
	private int idleConnectionTestPeriod = DEFAULT_IDLE_CONNECTION_TEST_PERIOD;
	/**异步执行sql线程池，默认8*/
	private int asyncPoolSize = DEFAULT_ASYNC_POOL_SIZE;
	/**po所在包路径，创建表时使用*/
	private String packagePath;
	/**是否打印SQL*/
	private boolean printSql = DEFAULT_PRINT_SQL;
	
	public static JDBCConfig newInstance(String jdbcConfigPath){
		return new JDBCConfig(new Setting(jdbcConfigPath, true));
	}
	
	private JDBCConfig(Setting jdbcSetting) {
		super();
		if (jdbcSetting.getStr("jdbc.url") != null) {
			url = jdbcSetting.getStr("jdbc.url");
			dbType = JdbcUtils.getDbType(url);
		}
		if (jdbcSetting.getStr("jdbc.username") != null) {
			username = jdbcSetting.getStr("jdbc.username");
		}
		if (jdbcSetting.getStr("jdbc.password") != null) {
			password = jdbcSetting.getStr("jdbc.password");
		}
		if (jdbcSetting.getStr("jdbc.driverClassName") != null) {
			driverClassName = jdbcSetting.getStr("jdbc.driverClassName");
			try {
				Class.forName(driverClassName);
			} catch (ClassNotFoundException e) {
				log.error("加载数据库驱动出错",e);
				throw new ConnectionException("加载数据库驱动出错",e);
			}
		}
		if (jdbcSetting.get("jdbc.minPoolSize") != null) {
			minPoolSize = jdbcSetting.getInt("jdbc.minPoolSize");
		}
		if (jdbcSetting.get("jdbc.maxPoolSize") != null) {
			maxPoolSize = jdbcSetting.getInt("jdbc.maxPoolSize");
		}
		if (jdbcSetting.get("jdbc.initialPoolSize") != null) {
			initialPoolSize = jdbcSetting.getInt("jdbc.initialPoolSize");
		}
		if (jdbcSetting.get("jdbc.executeTimeMonitor") != null) {
			executeTimeMonitor = jdbcSetting.getBool("jdbc.executeTimeMonitor");
		}
		if (jdbcSetting.get("jdbc.maxExecuteTime") != null) {
			maxExecuteTime = jdbcSetting.getLong("jdbc.maxExecuteTime");
		}
		if (jdbcSetting.get("jdbc.maxExecuteTimeFilePath") != null) {
			maxExecuteTimeFilePath = jdbcSetting.getStr("jdbc.maxExecuteTimeFilePath");
		}else{
			maxExecuteTimeFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
		}
		if (jdbcSetting.get("jdbc.maxWaitTime") != null) {
			maxWaitTime = jdbcSetting.getInt("jdbc.maxWaitTime");
		}
		if (jdbcSetting.get("jdbc.maxIdleTime") != null) {
			maxIdleTime = jdbcSetting.getInt("jdbc.maxIdleTime");
		}
		if (jdbcSetting.get("jdbc.idleConnectionTestPeriod") != null) {
			idleConnectionTestPeriod = jdbcSetting.getInt("jdbc.idleConnectionTestPeriod");
		}
		if (jdbcSetting.get("jdbc.asyncPoolSize") != null) {
			asyncPoolSize = jdbcSetting.getInt("jdbc.asyncPoolSize");
		}
		if (jdbcSetting.get("jdbc.packagePath") != null) {
			packagePath = jdbcSetting.getStr("jdbc.packagePath");
		}
		if (jdbcSetting.get("jdbc.printSql") != null) {
			printSql = jdbcSetting.getBool("jdbc.printSql");
		}
		if (jdbcSetting.get("jdbc.oncePollTime") != null) {
			oncePollTime = jdbcSetting.getInt("jdbc.oncePollTime");
		}

	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public int getInitialPoolSize() {
		return initialPoolSize;
	}

	public boolean getExecuteTimeMonitor() {
		return executeTimeMonitor;
	}

	public String getDbType() {
		return dbType;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public int getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public long getMaxExecuteTime() {
		return maxExecuteTime;
	}

	public String getMaxExecuteTimeFilePath() {
		return maxExecuteTimeFilePath;
	}

	public int getAsyncPoolSize() {
		return asyncPoolSize;
	}

	public String getPackagePath() {
		return packagePath;
	}

	public boolean getPrintSql() {
		return printSql;
	}

	public int getOncePollTime() {
		return oncePollTime;
	}
	
	

}