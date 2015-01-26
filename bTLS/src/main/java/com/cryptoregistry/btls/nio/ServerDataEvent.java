/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.nio;

import java.nio.channels.SocketChannel;

/**
 * This is the Rox tutorial code by James Greenfield
 * 
 * original source: http://rox-xmlrpc.sourceforge.net/niotut/#The code
 * 
 * @author Dave
 */
class ServerDataEvent {
	
	public final NioServer server;
	public final SocketChannel socket;
	public final byte[] data;
	
	public ServerDataEvent(NioServer server, SocketChannel socket, byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}