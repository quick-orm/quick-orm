package kim.zkp.quick.orm.exception;

public class SqlBuilderException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SqlBuilderException() {
		super();
	}

	public SqlBuilderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SqlBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public SqlBuilderException(String message) {
		super(message);
	}

	public SqlBuilderException(Throwable cause) {
		super(cause);
	}
	
	
}
