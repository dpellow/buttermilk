/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.kem;

import java.util.HashSet;
import java.util.Set;

import com.cryptoregistry.btls.handshake.UnexpectedCodeException;

/**
 * Useful base class - set up the listener plumbing
 * 
 * @author Dave
 *
 */
public class BaseKeyExchangeModule implements KeyExchangeModule {
	
	protected Set<KeyExchangeListener> exchangeListeners;

	public BaseKeyExchangeModule() {
		exchangeListeners = new HashSet<KeyExchangeListener>();
	}
	
	public void addKeyExchangeListener(KeyExchangeListener listener){
		this.exchangeListeners.add(listener);
	}

	@Override
	public boolean exchange() throws ExchangeFailedException,
			UnexpectedCodeException {
		// TODO Auto-generated method stub
		return false;
	}

}
