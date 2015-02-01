/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.kem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cryptoregistry.btls.handshake.Handshake;
import com.cryptoregistry.btls.handshake.HandshakeProtocol;
import com.cryptoregistry.btls.handshake.UnexpectedCodeException;
import com.cryptoregistry.symmetric.SymmetricKeyContents;

/**
 * For testing purposes - hard code the shared secret key
 * 
 * @author Dave
 *
 */
public class H0KeyExchangeModule extends BaseKEM {
	
	static final Logger logger = LogManager.getLogger(H0KeyExchangeModule.class.getName());

	// container reference
	Handshake handshake;
	
	private SymmetricKeyContents resultKey = Handshake.predefinedResult(HandshakeProtocol.H0);
	
	public H0KeyExchangeModule(Handshake handshake) {
		super();
		this.handshake = handshake;
	}

	@Override
	public boolean exchange() throws ExchangeFailedException, UnexpectedCodeException {
		return true;
	}

	protected void buildKey() {
		for(KeyExchangeListener l: exchangeListeners){
			l.secretExchangeCompleted(new KeyExchangeEvent(this, resultKey));
		}
	}

	public SymmetricKeyContents getResultKey() {
		return resultKey;
	}
	
}
