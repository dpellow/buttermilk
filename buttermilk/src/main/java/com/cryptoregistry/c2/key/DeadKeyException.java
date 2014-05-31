/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2.key;

/**
 * Thrown if an attempt is made to access key data which has already been cleaned up 
 * (a call to selfDestruct() has been made previously against that key)
 * 
 * @author Dave
 *
 */
public class DeadKeyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeadKeyException() {
		super();
	}

	public DeadKeyException(String message) {
		super(message);
	}

	public DeadKeyException(Throwable cause) {
		super(cause);
	}

	public DeadKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeadKeyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
