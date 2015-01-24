/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.validator.key;

import java.util.HashSet;
import java.util.Set;

/**
 * Useful base class - set up the listener plumbing. Notify interested
 * parties if validation was successful
 * 
 * @author Dave
 *
 */
public abstract class BaseKeyValidator implements KeyValidator {
	
	protected Set<KeyValidationListener> validationListeners;

	public BaseKeyValidator() {
		validationListeners = new HashSet<KeyValidationListener>();
	}
	
	public void addKeyExchangeListener(KeyValidationListener listener){
		this.validationListeners.add(listener);
	}

	
	public abstract void validate();

}
