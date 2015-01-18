/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.ekem;

import com.cryptoregistry.btls.handshake.kem.ExchangeFailedException;

public class PassthroughEphemeralKeyExchangeModule implements EphemeralKeyExchangeModule {

	public PassthroughEphemeralKeyExchangeModule() {
	}

	@Override
	public void exchange() throws ExchangeFailedException {
		// do nothing

	}

}
