/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.kem;

import java.util.EventObject;

import com.cryptoregistry.symmetric.SymmetricKeyContents;

/**
 * Event used by the KeyExchangeListener (encapsulates the shared secret key). 
 * 
 * @author Dave
 *
 */
public class KeyExchangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private SymmetricKeyContents contents;

	public KeyExchangeEvent(Object source, SymmetricKeyContents contents) {
		super(source);
		this.contents = contents;
	}

	public SymmetricKeyContents getContents() {
		return contents;
	}

}
