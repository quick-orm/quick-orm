package com.z.quick.orm.connection;

public interface DefaultConfig {
	
	public final static int DEFAULT_INITIAL_POOL_SIZE = 1;
	public final static int DEFAULT_MAX_POOL_SIZE = 8;
	public final static int DEFAULT_MIN_POOL_SIZE = 2;
	/**单位毫秒 当连接池连接耗尽时，客户端调用getConnection()后等待获取新连接的时间，超时后将抛出RuntimeException，如设为0则无限期等待。单位毫秒。默认: 10000*/
	public final static int DEFAULT_MAX_WAIT = 10*1000;
	/**最大空闲时间,单位毫秒，默认30分钟*/
	public final static int DEFAULT_MAX_IDLE_TIME = 30*60*1000;
	/**单位毫秒，间隔多少时间检查所有连接池中的空闲连接。默认值: 10分钟检查一次，为0不检查*/
	public final static int DEFAULT_IDLE_CONNECTION_TEST_PERIOD = 10*60*1000;
	public final static boolean DEFAULT_EXECUTE_TIME_MONITOR = false;
	
	

}
