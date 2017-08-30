package com.z.quick.orm.session;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureSession implements FutureDataBaseManipulation {

	private DataBaseManipulation session;
	private final ExecutorService threadPool;

	public FutureSession(DataBaseManipulation session, int poolSize) {
		super();
		this.session = session;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public Future<Integer> save(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.save(o);
			}
		});
	}
	
	@Override
	public Future<Integer> delete(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.delete(o);
			}
		});
	}

	@Override
	public Future<Integer> update(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.update(o);
			}
		});
	}

	@Override
	public Future<Object> get(Object o) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return session.get(o);
			}
		});
	}

	@Override
	public Future<Object> get(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return session.get(o, clzz);
			}
		});
	}

	@Override
	public Future<List<Object>> list(Object o) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return session.list(o);
			}
		});
	}

	@Override
	public Future<List<Object>> list(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return session.list(o, clzz);
			}
		});
	}

	@Override
	public Future<Integer> save(String sql, Object...params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.save(sql, params);
			}
		});
	}
	
	@Override
	public Future<Integer> delete(String sql, Object...params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.update(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> update(String sql, Object...params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return session.update(sql, params);
			}
		});
	}

	@Override
	public Future<Object> get(String sql, Class<?> clzz, Object...params) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return session.get(sql, clzz, params);
			}
		});
	}

	@Override
	public Future<List<Object>> list(String sql, Class<?> clzz, Object...params) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return session.list(sql, clzz, params);
			}
		});
	}

}
