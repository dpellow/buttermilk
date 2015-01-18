/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.kem;

import com.cryptoregistry.btls.handshake.UnexpectedCodeException;

/**
 * Classes which implement KeyExchangeModule perform a 
 * <a href="http://en.wikipedia.org/wiki/Key_exchange">key exchange</a> function of some kind
 *   
 * @author Dave
 *
 */
public interface KeyExchangeModule {

	boolean exchange() throws ExchangeFailedException, UnexpectedCodeException;
}
