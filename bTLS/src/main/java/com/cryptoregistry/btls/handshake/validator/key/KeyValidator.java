/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.validator.key;

import com.cryptoregistry.btls.handshake.validator.KeyValidationListener;

public interface KeyValidator {

	public void validate();
	public void addKeyExchangeListener(KeyValidationListener listener);
	
}
