/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake;

public class HandshakeFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	public HandshakeFailedException() {
	}

	public HandshakeFailedException(String message) {
		super(message);
	}

	public HandshakeFailedException(Throwable cause) {
		super(cause);
	}

	public HandshakeFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public HandshakeFailedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
