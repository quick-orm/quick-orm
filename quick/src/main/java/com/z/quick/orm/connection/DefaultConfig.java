package com.z.quick.orm.connection;

public interface DefaultConfig {
	
	public final static int DEFAULT_INITIAL_POOL_SIZE = 1;
	public final static int DEFAULT_MAX_POOL_SIZE = 8;
	public final static int DEFAULT_MIN_POOL_SIZE = 1;
	/**单位毫秒 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出ConnectionException，如设为0则无限期等待。单位毫秒。默认: 10000*/
	public final static int DEFAULT_MAX_WAIT_TIME = 10*1000;
	/**最大空闲时间,单位毫秒，默认30分钟*/
	public final static int DEFAULT_MAX_IDLE_TIME = 30*60*1000;
	/**当连接池连接耗尽时，每次轮询获取连接的间隔时间，默认50毫秒*/
	public final static int DEFAULT_ONCE_POLL_TIME = 50;
	/**单位毫秒，间隔多少时间检查所有连接池中的空闲连接。默认值: 10分钟检查一次，为0不检查*/
	public final static int DEFAULT_IDLE_CONNECTION_TEST_PERIOD = 10*60*1000;
	/**是否启用sql执行时间监控*/
	public final static boolean DEFAULT_EXECUTE_TIME_MONITOR = false;
	/**单位毫秒 sql执行最大耗时，超过此耗时的sql会被记录在文件中*/
	public final static long DEFAULT_MAX_EXECUTE_TIME = 2000;
	/**最大耗时sql保存文件路径 默认classes目录下*/
	public final static String DEFAULT_MAX_EXECUTE_TIME_FILE_PATH = "/";
	/**异步执行sql线程池，默认8*/
	public final static int DEFAULT_ASYNC_POOL_SIZE = 8;
	/**是否打印SQL 默认true*/
	public final static boolean DEFAULT_PRINT_SQL = true;
	
	

}
