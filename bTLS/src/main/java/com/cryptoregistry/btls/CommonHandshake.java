package com.cryptoregistry.btls;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Encapsulate the part of the handleshake which is common in every case:
 * 
 * 1) Client - HelloProto registration handle client key handle client 2) Server
 * - Hello Proto | Alert - fail registration handle server key handle server or
 * Alert - fail
 * 
 * we search for key locally or possibly online. If found, send key found, else
 * send key required
 * 
 * 
 * @author Dave
 *
 */
public class CommonHandshake {

	// parent I/O from the Socket
	InputStream input;
	OutputStream output;

	// true if this is a server-side socket
	boolean isServer;

	public CommonHandshake(boolean isServer, 
			InputStream input,
			OutputStream output) {
		this.isServer = isServer;
		this.input = input;
		this.output = output;
	}
}
