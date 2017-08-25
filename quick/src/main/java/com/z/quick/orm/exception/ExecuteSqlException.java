package com.z.quick.orm.exception;

public class ExecuteSqlException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExecuteSqlException() {
		super();
	}

	public ExecuteSqlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExecuteSqlException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecuteSqlException(String message) {
		super(message);
	}

	public ExecuteSqlException(Throwable cause) {
		super(cause);
	}
	
	
}
