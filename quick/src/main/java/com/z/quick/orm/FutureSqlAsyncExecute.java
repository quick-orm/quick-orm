package com.z.quick.orm;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureSqlAsyncExecute implements SqlAsyncExecute{
	
	private SqlExecute sqlExecute;
	private final ExecutorService threadPool;
	
	public FutureSqlAsyncExecute(SqlExecute sqlExecute, int poolSize) {
		super();
		this.sqlExecute = sqlExecute;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public Future<Integer> save(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlExecute.update(o);
			}
		});
	}

	@Override
	public Future<Integer> update(Object o) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlExecute.update(o);
			}
		});
	}

	@Override
	public Future<Object> get(Object o) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return sqlExecute.get(o);
			}
		});
	}

	@Override
	public Future<Object> get(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return sqlExecute.get(o,clzz);
			}
		});
	}

	@Override
	public Future<List<Object>> list(Object o) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return sqlExecute.list(o);
			}
		});
	}

	@Override
	public Future<List<Object>> list(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return sqlExecute.list(o,clzz);
			}
		});
	}

	@Override
	public Future<Integer> save(String sql, Object[] params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlExecute.save(sql, params);
			}
		});
	}

	@Override
	public Future<Integer> update(String sql, Object[] params) {
		return threadPool.submit(new Callable<Integer>() {
			public Integer call() throws Exception {
				return sqlExecute.update(sql, params);
			}
		});
	}

	@Override
	public Future<Object> get(String sql, Class<?> clzz, Object[] params) {
		return threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return sqlExecute.get(sql, clzz, params);
			}
		});
	}

	@Override
	public Future<List<Object>> list(String sql, Class<?> clzz, Object[] params) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return sqlExecute.list(sql, clzz, params);
			}
		});
	}

}
