/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.ekem;

import java.util.HashSet;
import java.util.Set;

import com.cryptoregistry.btls.handshake.UnexpectedCodeException;
import com.cryptoregistry.btls.handshake.kem.ExchangeFailedException;
import com.cryptoregistry.btls.handshake.kem.KeyExchangeListener;

/**
 * Useful base class - set up the listener plumbing
 * 
 * @author Dave
 *
 */
public class BaseEKEM implements EphemeralKeyExchangeModule {
	
	protected Set<KeyExchangeListener> exchangeListeners;

	public BaseEKEM() {
		exchangeListeners = new HashSet<KeyExchangeListener>();
	}
	
	public void addKeyExchangeListener(KeyExchangeListener listener){
		this.exchangeListeners.add(listener);
	}

	@Override
	public boolean exchange() throws ExchangeFailedException {
		// TODO Auto-generated method stub
		return false;
	}

}
