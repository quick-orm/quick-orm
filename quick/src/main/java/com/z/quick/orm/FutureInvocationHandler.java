package com.z.quick.orm;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureInvocationHandler implements InvocationHandler {
	private static final ExecutorService threadPool = Executors.newSingleThreadExecutor();
	private SQLExecute target;

	public FutureInvocationHandler() {
		super();
	}

	public FutureInvocationHandler(SQLExecute target) {
		super();
		this.target = target;
	}

	@Override
	public Object invoke(Object o, Method method, Object[] args) throws Throwable {
		Future<Object> future = threadPool.submit(new Callable<Object>() {
			public Object call() throws Exception {
				return method.invoke(target, args);
			}
		});
		return future;

	}
}