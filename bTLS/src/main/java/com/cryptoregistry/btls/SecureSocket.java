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

import x.org.bouncycastle.crypto.io.*;

import org.apache.log4j.Logger;


/**
 * 
 * A secure socket using contemporary techniques.
 * 
 * @author Dave
 *
 */
public class SecureSocket extends Socket {
	
	private static final Logger log = Logger.getLogger("com.cryptography.btls.C2Socket");
	
	CipherOutputStream cout;
	CipherInputStream cin;

	//package-protected constructor, used only by server socket
	SecureSocket() throws IOException {
		super();
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
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public SecureSocket(String host, int port) throws UnknownHostException,
			IOException {
		super(host, port);
	}
	
	/**
	 * Package protected, set by handshake completion
	 * 
	 * @param cin
	 * @param cout
	 */
	void setStreams(Handshake handshake) {
		this.cin = handshake.decorateInputStream();
		this.cout = handshake.decorateOutputStream();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		return cout;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		return cin;
	}
	
	/**
	 * Package protected, called only within Server socket
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream getRawInputStream() throws IOException {
		return super.getInputStream();
	}
	
	/**
	 * Package protected, called only within Server socket
	 * 
	 * @return
	 * @throws IOException
	 */
	
	OutputStream getRawOutputStream() throws IOException {
		return super.getOutputStream();
	}
	
	/**
	 * Required to call to complete the cipher text
	 * 
	 */
	public synchronized void close() throws IOException {
		if(!this.isClosed()){
			// forces a doFinal() and flush in the cipher engine
			cout.close();
			
			// allow a little time for the socket to do final process before closing
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//now close the socket
			log.info("closing "+this);
			super.close();
		}
	}

}
