package com.z.quick.orm.session;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureSession{
	
	private Session session;
	private final ExecutorService threadPool;
	
	public FutureSession(Session session, int poolSize) {
		super();
		this.session = session;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	
	public Future<Integer> save(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.save(o);
			}
		});
	}

	
	public Future<Integer> update(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.update(o);
			}
		});
	}

	
	public Future<Object> get(Object o) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return session.get(o);
			}
		});
	}

	
	public Future<Object> get(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return session.get(o,clzz);
			}
		});
	}

	
	public Future<List<Object>> list(Object o) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return session.list(o);
			}
		});
	}

	
	public Future<List<Object>> list(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return session.list(o,clzz);
			}
		});
	}

	
	public Future<Integer> save(String sql, Object[] params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.save(sql, params);
			}
		});
	}

	
	public Future<Integer> update(String sql, Object[] params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.update(sql, params);
			}
		});
	}

	
	public Future<Object> get(String sql, Class<?> clzz, Object[] params) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return session.get(sql, clzz, params);
			}
		});
	}

	
	public Future<List<Object>> list(String sql, Class<?> clzz, Object[] params) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return session.list(sql, clzz, params);
			}
		});
	}

	
	public Future<Integer> delete(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.delete(o);
			}
		});
	}

	
	public Future<Integer> delete(String sql, Object[] params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.update(sql, params);
			}
		});
	}

}
