/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.kem;

/**
 * Thrown when a KEM exchange fails
 * 
 * @author Dave
 *
 */
public class ExchangeFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExchangeFailedException() {
	}

	public ExchangeFailedException(String message) {
		super(message);
	}

	public ExchangeFailedException(Throwable cause) {
		super(cause);
	}

	public ExchangeFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExchangeFailedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
