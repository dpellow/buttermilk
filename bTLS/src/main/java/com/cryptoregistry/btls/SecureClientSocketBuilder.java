/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cryptoregistry.btls.handshake.BasicHandshake;
import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.btls.handshake.HandshakeProtocol;
import com.cryptoregistry.btls.handshake.init.BasicAutoloader;
import com.cryptoregistry.client.security.Datastore;

/**
 * Instead of extending Socket, bTLS uses the Decorator pattern on sockets to
 * wrap the default SocketImpl class. The wrapper delegates most
 * of Socket's methods and overrides a few to return FrameInputStream and
 * FrameOutputStreams for I/O, and also to override close().
 * 
 * @author Dave
 *
 */
public class SecureClientSocketBuilder {

	static final Logger logger = LogManager
			.getLogger(SecureClientSocketBuilder.class.getName());

	final Socket socket;
	final Datastore ds;
	final HandshakeProtocol protocol;

	/**
	 * Package-protected, Used within SecureServerSocket
	 * 
	 * @param ds
	 * @param client
	 */
	SecureClientSocketBuilder(Datastore ds, Socket client) {
		this.socket = client;
		this.ds = ds;
		this.protocol = null;
	}

	/**
	 * For client use
	 * 
	 * @param protocol
	 * @param ds
	 * @param client
	 */
	public SecureClientSocketBuilder(HandshakeProtocol protocol,
			Datastore ds, Socket client) {
		this.socket = client;
		this.ds = ds;
		this.protocol = protocol;
	}

	/**
	 * Clients call this one
	 * 
	 * @return
	 * @throws HandshakeFailedException
	 */
	public Socket buildSecure() throws HandshakeFailedException, IOException {

		// wrapper listens for events in handshake subcomponents
		SecureSocketWrapper wrapper = new SecureSocketWrapper(socket);
		BasicHandshake h = new BasicHandshake(protocol, ds);
		h.setIn(socket.getInputStream());
		h.setOut(socket.getOutputStream());
		BasicAutoloader autoloader = new BasicAutoloader(h);
		h.setAutoloader(autoloader);
		autoloader.addAutoloadListener(wrapper);
		h.doHandshake();

		// by end of handshake, wrapper will have been set up
		return wrapper;
	}

	/**
	 * Package-protected, used within SecureServerSocket
	 * 
	 * @return
	 * @throws HandshakeFailedException
	 */
	Socket buildServerSecure() throws HandshakeFailedException, IOException {
		// wrapper listens for events in handshake subcomponents
		SecureSocketWrapper wrapper = new SecureSocketWrapper(socket);
		BasicHandshake h = new BasicHandshake(ds);
		h.setIn(socket.getInputStream());
		h.setOut(socket.getOutputStream());
		BasicAutoloader autoloader = new BasicAutoloader(h);
		h.setAutoloader(autoloader);
		autoloader.addAutoloadListener(wrapper);
		h.doHandshake();

		// by end of handshake, wrapper will have been set up
		return wrapper;
	}

}
