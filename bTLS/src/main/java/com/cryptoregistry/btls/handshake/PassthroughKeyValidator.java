/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake;

public class PassthroughKeyValidator implements KeyValidator {

	public PassthroughKeyValidator() {
	}

	/**
	 * Always returns true
	 * 
	 */
	@Override
	public boolean validate() {
		return true;
	}

}
