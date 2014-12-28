package com.cryptoregistry.client.security;

public class SuitableMatchFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public SuitableMatchFailedException() {
	}

	public SuitableMatchFailedException(String message) {
		super(message);
	}

	public SuitableMatchFailedException(Throwable cause) {
		super(cause);
	}

	public SuitableMatchFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public SuitableMatchFailedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
