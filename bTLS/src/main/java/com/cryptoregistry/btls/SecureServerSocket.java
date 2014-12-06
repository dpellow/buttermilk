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

import com.cryptoregistry.c2.key.Curve25519KeyContents;

/**
 * A secure socket using contemporary techniques.
 * 
 * @author Dave
 *
 */
public class SecureServerSocket extends ServerSocket {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.C2ServerSocket");

	Curve25519KeyContents serverKey;

	public SecureServerSocket(Curve25519KeyContents serverKey) throws IOException {
		super();
		this.serverKey=serverKey;
	}

	public SecureServerSocket(Curve25519KeyContents serverKey, int port) throws IOException {
		super(port);
		this.serverKey=serverKey;
	}

	public SecureServerSocket(Curve25519KeyContents serverKey, int port, int backlog) throws IOException {
		super(port, backlog);
		this.serverKey=serverKey;
	}

	public SecureServerSocket(Curve25519KeyContents serverKey, int port, int backlog, InetAddress bindAddress)
			throws IOException {
		super(port, backlog, bindAddress);
		this.serverKey=serverKey;
	}

	/**
	 * Performs the server-side handshake prior to returning a C2Socket
	 */
	public Socket accept() throws IOException {
		log.trace("entering accept");
		
		SecureSocket s = new SecureSocket();
		implAccept(s);
		C2Handshake handshake = new C2Handshake(true, serverKey, s.getRawInputStream(),s.getRawOutputStream());
		if(!handshake.serversHandshake()){
			// failed, throw exception
			throw new IOException("Handshake failed: "+s.toString());
		}else{
			// handshake also knows how to set up the Cipher stream decorators we will use
			s.setStreams(handshake);
		}
		return s;
	}

}
