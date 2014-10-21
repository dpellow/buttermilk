package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BTLSSocket extends Socket {

	public BTLSSocket(InetAddress address, int port) throws IOException {
		super(address, port);
	}

	public BTLSSocket(String host, int port) throws UnknownHostException,
			IOException {
		super(host, port);
	}

	

}
