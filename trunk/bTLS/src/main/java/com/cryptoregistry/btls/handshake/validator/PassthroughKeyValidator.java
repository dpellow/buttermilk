/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.validator;

public class PassthroughKeyValidator extends BaseKeyValidator {

	public PassthroughKeyValidator() {
	}

	/**
	 * Does nothing but send true
	 * 
	 */
	@Override
	public void validate() {
		for(ValidationListener l: this.validationListeners) {
			l.validationResult(new ValidationEvent(this)); // hard coded to true
		}
	}

}