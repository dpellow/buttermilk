/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.validator.key;

import com.cryptoregistry.btls.handshake.validator.KeyValidationListener;
import com.cryptoregistry.btls.handshake.validator.ValidationEvent;

public class PassthroughKeyValidator extends BaseKeyValidator {

	public PassthroughKeyValidator() {
	}

	/**
	 * Does nothing but send true
	 * 
	 */
	@Override
	public void validate() {
		for(KeyValidationListener l: this.validationListeners) {
			l.keyValidationResult(new ValidationEvent(this)); // hard coded to true
		}
	}

}
