/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.validator.digest;

import java.util.HashSet;
import java.util.Set;

import com.cryptoregistry.btls.handshake.validator.DigestValidationListener;

/**
 * Useful base class - set up the listener plumbing. Notify interested
 * parties if validation was successful
 * 
 * @author Dave
 *
 */
public abstract class BaseDigestValidator implements DigestValidator {
	
	protected Set<DigestValidationListener> validationListeners;

	public BaseDigestValidator() {
		validationListeners = new HashSet<DigestValidationListener>();
	}
	
	public void addDigestValidationListener(DigestValidationListener listener){
		this.validationListeners.add(listener);
	}

	
	public abstract void validate();

}
