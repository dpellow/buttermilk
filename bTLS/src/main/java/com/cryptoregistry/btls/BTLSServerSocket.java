package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.c2.key.Curve25519KeyContents;

public class BTLSServerSocket extends ServerSocket {

	Lock lock = new ReentrantLock();
	Curve25519KeyContents serverKey;
	
	SecureRandom rand = new SecureRandom();

	public BTLSServerSocket() throws IOException {
		super();
		serverKey = Configuration.CONFIG.serverKey();
	}

	public BTLSServerSocket(int port) throws IOException {
		super(port);
		serverKey = Configuration.CONFIG.serverKey();
	}

	public BTLSServerSocket(int arg0, int arg1) throws IOException {
		super(arg0, arg1);
		serverKey = Configuration.CONFIG.serverKey();
	}

	public BTLSServerSocket(int arg0, int arg1, InetAddress arg2)
			throws IOException {
		super(arg0, arg1, arg2);
		serverKey = Configuration.CONFIG.serverKey();
	}

	/**
	 * Performs the server-side handshake prior to returning - blocks
	 */
	public Socket accept() throws IOException {
		byte [] randBytes = new byte[32];
		rand.nextBytes(randBytes);
		BTLSSocket s = new BTLSSocket(serverKey, randBytes);
		implAccept(s);
		s.serversHandshake();
		return s;
	}

}
