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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.client.security.Datastore;

/**
 * A secure socket using contemporary techniques.
 * 
 * @author Dave
 *
 */
public class SecureServerSocket extends ServerSocket {

	static final Logger logger = LogManager.getLogger(SecureServerSocket.class.getName());
	
	protected Datastore ds; // our key cache
	
	public SecureServerSocket(Datastore ds) throws IOException {
		super();
		this.ds = ds;
	}

	public SecureServerSocket(Datastore ds, int port) throws IOException {
		super(port);
		this.ds = ds;
	}

	public SecureServerSocket(Datastore ds, int port, int backlog) throws IOException {
		super(port, backlog);
		this.ds = ds;
	}

	public SecureServerSocket(Datastore ds, int port, int backlog, InetAddress bindAddress)
			throws IOException {
		super(port, backlog, bindAddress);
		this.ds = ds;
	}

	/**
	 * Performs the server-side handshake prior to returning a SecureSocket
	 */
	public Socket accept() throws IOException {
		if(ds == null) throw new RuntimeException("Datastore is null!");
		logger.trace("entering accept");
		
		Socket s = new Socket();
		implAccept(s);
	
		ClientSocketSecureConnector connector = new ClientSocketSecureConnector(ds,s);
		try {
			return connector.connectServerSecure();
		} catch (HandshakeFailedException e) {
			throw new IOException(e);
		}
	}

}
