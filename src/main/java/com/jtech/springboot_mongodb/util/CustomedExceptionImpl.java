package com.jtech.springboot_mongodb.util;

public class CustomedExceptionImpl extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5534060774742162622L;
	private int errorCode;
	private static String DELIMITER = "|";
	private String arg[];
	private String message;

	public CustomedExceptionImpl(String message) {
		setMessage(message);
		errorCode = -1;
		arg = null;
	}

	public CustomedExceptionImpl(int code, String message) {
		setMessage(message);
		setErrorCode(code);
		arg = null;
	}

	public CustomedExceptionImpl(int code, String arg[]) {
		this.errorCode = code;
		this.arg = arg;
	}

	public CustomedExceptionImpl() {
		errorCode = -1;
		arg = null;
		message = "";
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getArg() {
		if (arg == null)
			return null;
		String msg = "";
		for (int i = 0; i < arg.length; i++)
			if (i == 0)
				msg = arg[i];
			else
				msg = msg + DELIMITER + arg[i];

		return msg;
	}

	public void setError(int code, String message) {
		this.errorCode = code;
		this.message = message;
	}

}
