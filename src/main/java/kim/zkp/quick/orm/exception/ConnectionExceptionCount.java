package kim.zkp.quick.orm.exception;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionExceptionCount {
	private static final Integer CONNECTION_ERROR_NUM = 3;
	private static final Map<Connection,AtomicInteger> excepCache = new HashMap<Connection, AtomicInteger>();
	
	public static void add(Connection c){
		AtomicInteger a = excepCache.get(c);
		if (a == null) {//允许并发情况下的数据差错
			a = new AtomicInteger(0);
			excepCache.put(c, a);
		}
		a.incrementAndGet();
	}
	public static void destroy(Connection c){
		AtomicInteger a = excepCache.get(c);
		if (a == null) {//允许并发情况下的数据差错
			a = new AtomicInteger(0);
			excepCache.put(c, a);
		}
		a.set(CONNECTION_ERROR_NUM);
	}
	public static Integer get(Connection c){
		try {
			return excepCache.get(c).get();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Boolean isClose(Connection c){
		if (excepCache.get(c) == null) {
			return false;
		}
		if (excepCache.get(c).get()>=CONNECTION_ERROR_NUM) {
			remove(c);
			return true;
		}
		return false;
	}
	public static void remove(Connection c){
		excepCache.remove(c);
	}
}
