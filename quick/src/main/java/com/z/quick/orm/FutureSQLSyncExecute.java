package com.z.quick.orm;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureSQLSyncExecute implements SQLAsyncExecute{
	
	private SQLExecute sqlExecute;
	private final ExecutorService threadPool;
	
	public FutureSQLSyncExecute(SQLExecute sqlExecute, int poolSize) {
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
	public Future<List<Object>> find(Object o) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return sqlExecute.find(o);
			}
		});
	}


	@Override
	public Future<List<Object>> find(Object o, Class<?> clzz) {
		return threadPool.submit(new Callable<List<Object>>() {
			public List<Object> call() throws Exception {
				return sqlExecute.find(o,clzz);
			}
		});
	}


}
