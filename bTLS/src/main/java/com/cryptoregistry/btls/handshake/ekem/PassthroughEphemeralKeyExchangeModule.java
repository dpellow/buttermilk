/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.ekem;

import com.cryptoregistry.btls.handshake.kem.ExchangeFailedException;

public class PassthroughEphemeralKeyExchangeModule extends BaseEKEM {

	public PassthroughEphemeralKeyExchangeModule() {
		super();
	}

	@Override
	public boolean exchange() throws ExchangeFailedException {
		// always returns true
		return true;
	}

	
}
