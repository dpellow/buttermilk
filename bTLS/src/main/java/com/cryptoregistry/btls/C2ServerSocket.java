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
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.cryptoregistry.c2.key.Curve25519KeyContents;

/**
 * A secure socket using contemporary techniques
 * 
 * @author Dave
 *
 */
public class C2ServerSocket extends ServerSocket {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.C2ServerSocket");
	
	Lock lock = new ReentrantLock();
	Curve25519KeyContents serverKey;
	
	SecureRandom rand = new SecureRandom();

	public C2ServerSocket(Curve25519KeyContents serverKey) throws IOException {
		super();
		this.serverKey=serverKey;
	}

	public C2ServerSocket(Curve25519KeyContents serverKey, int port) throws IOException {
		super(port);
		this.serverKey=serverKey;
	}

	public C2ServerSocket(Curve25519KeyContents serverKey, int arg0, int arg1) throws IOException {
		super(arg0, arg1);
		this.serverKey=serverKey;
	}

	public C2ServerSocket(Curve25519KeyContents serverKey, int arg0, int arg1, InetAddress arg2)
			throws IOException {
		super(arg0, arg1, arg2);
		this.serverKey=serverKey;
	}

	/**
	 * Performs the server-side handshake prior to returning a C2Socket
	 */
	public Socket accept() throws IOException {
		log.trace("entering accept");
		C2Socket s = new C2Socket(serverKey);
		implAccept(s);
		s.serversHandshake();
		return s;
	}

}
