/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * A secure socket using contemporary techniques.
 * 
 * @author Dave
 *
 */
public class SecureServerSocket extends ServerSocket {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.SecureServerSocket");

	public SecureServerSocket() throws IOException {
		super();
	}

	public SecureServerSocket(int port) throws IOException {
		super(port);
	}

	public SecureServerSocket(int port, int backlog) throws IOException {
		super(port, backlog);
	}

	public SecureServerSocket(int port, int backlog, InetAddress bindAddress)
			throws IOException {
		super(port, backlog, bindAddress);
	}

	/**
	 * Performs the server-side handshake prior to returning a C2Socket
	 */
	public Socket accept() throws IOException {
		log.trace("entering accept");
		
		SecureSocket s = new SecureSocket();
		implAccept(s);
	
		// do handshake
	
		return s;
	}

}
