/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.kem;

/**
 * Components which listen to this interface will get a copy of the secret key exchange result if they
 * have registered with the KEM.
 * 
 * @author Dave
 *
 */
public interface KeyExchangeListener {

	public void keyExchangeCompleted(KeyExchangeEvent evt);
}
