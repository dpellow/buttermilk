/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature;

/**
 * Hard exception - thrown when the process of finding signing data fails.
 *  
 * @author Dave
 *
 */
public class RefNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public RefNotFoundException() {}

	public RefNotFoundException(String message) {
		super(message);
	}

	public RefNotFoundException(Throwable cause) {
		super(cause);
	}

	public RefNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RefNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
