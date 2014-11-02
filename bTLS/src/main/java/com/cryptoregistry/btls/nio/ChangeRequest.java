/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.nio;

import java.nio.channels.SocketChannel;

/**
 * This is the Rox tutorial code by James Greenfield, updated a bit and secured with Curve25519.
 * 
 * original source: http://rox-xmlrpc.sourceforge.net/niotut/#The code
 * 
 * @author Dave
 */
public class ChangeRequest {
	public static final int REGISTER = 1;
	public static final int CHANGEOPS = 2;
	
	public SocketChannel socket;
	public int type;
	public int ops;
	
	public ChangeRequest(SocketChannel socket, int type, int ops) {
		this.socket = socket;
		this.type = type;
		this.ops = ops;
	}
}
