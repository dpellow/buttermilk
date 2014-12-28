/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.cryptoregistry.btls.io.FrameInputStream;
import com.cryptoregistry.btls.io.FrameOutputStream;
import com.cryptoregistry.client.security.DataStore;

/**
 * 
 * A secure socket using contemporary techniques. This socket uses standard java blocking I/O
 * 
 * @author Dave
 *
 */
public class SecureSocket extends Socket {
	
	private static final Logger log = Logger.getLogger("com.cryptography.btls.SecureSocket");
	
	protected FrameOutputStream fout;
	protected FrameInputStream fin;
	protected DataStore ds; // our key cache, must be set prior to use

	//package-protected constructor, used only by server socket
	SecureSocket() throws IOException {
		super();
		fin = new FrameInputStream(super.getInputStream());
		fout = new FrameOutputStream(super.getOutputStream());
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public SecureSocket(InetAddress address, int port) throws IOException {
		super(address, port);
		fin = new FrameInputStream(super.getInputStream());
		fout = new FrameOutputStream(super.getOutputStream());
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public SecureSocket(String host, int port) throws UnknownHostException, IOException {
		super(host, port);
		fin = new FrameInputStream(super.getInputStream());
		fout = new FrameOutputStream(super.getOutputStream());
	}
	
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return fout;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return fin;
	}
	
	public synchronized void close() throws IOException {
		if(!this.isClosed()){
			fout.close();
			//now close the socket
			log.info("closing "+this);
			super.close();
		}
	}

	public void setDs(DataStore ds) {
		this.ds = ds;
	}

}
