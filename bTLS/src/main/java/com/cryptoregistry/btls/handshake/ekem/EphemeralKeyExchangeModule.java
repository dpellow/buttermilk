/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.ekem;

import com.cryptoregistry.btls.handshake.kem.ExchangeFailedException;

/**
 * Classes implementing this interface do <a href="http://en.wikipedia.org/wiki/Key_encapsulation">key encapsulation</>
 *  in one form or another based on the type of handshake algorithm
 * 
 * @author Dave
 *
 */
public interface EphemeralKeyExchangeModule {

	public boolean exchange() throws ExchangeFailedException;
}
