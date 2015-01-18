/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.init;

import com.cryptoregistry.btls.handshake.HandshakeFailedException;

/**
 * Once the protocol is made known to the server, it has to set up the various subcomponents
 * appropriate to it. This process is conceived as dynamic - the server may or may not support 
 * the given requested handshake algorithm. 
 * 
 * @author Dave
 *
 */
public interface Autoloader {

	public void load() throws HandshakeFailedException ;
	
}
