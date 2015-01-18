/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake;

public class UnexpectedCodeException extends Exception {

	
	private static final long serialVersionUID = 1L;
	int code;

	public UnexpectedCodeException(int code) {
		this.code = code;
	}

	public UnexpectedCodeException(String message) {
		super(message);
	}

	public UnexpectedCodeException(Throwable cause) {
		super(cause);
	}

	public UnexpectedCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedCodeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
