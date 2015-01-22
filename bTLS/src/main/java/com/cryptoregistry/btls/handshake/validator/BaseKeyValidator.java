/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.validator;

import java.util.HashSet;
import java.util.Set;

import com.cryptoregistry.btls.handshake.UnexpectedCodeException;

/**
 * Useful base class - set up the listener plumbing. Notify interested
 * parties if validation was successful
 * 
 * @author Dave
 *
 */
public class BaseKeyValidator implements KeyValidator {
	
	protected Set<ValidationListener> validationListeners;

	public BaseKeyValidator() {
		validationListeners = new HashSet<ValidationListener>();
	}
	
	public void addKeyExchangeListener(ValidationListener listener){
		this.validationListeners.add(listener);
	}

	// does nothing
	@Override
	public void validate()  {
	
	}

}
