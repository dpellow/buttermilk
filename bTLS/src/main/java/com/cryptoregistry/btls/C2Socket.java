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
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.frame.C2KeyForPublicationOutputFrame;
import com.cryptoregistry.proto.frame.InputFrameReader;

/**
 * A secure socket using contemporary techniques
 * 
 * @author Dave
 *
 */
public class C2Socket extends Socket {
	
	private static final Logger log = Logger.getLogger("com.cryptography.btls.C2Socket");
	
	Lock lock = new ReentrantLock();
	
	// asymmetric keys used in handshake. one will be only the "public" portion
	Curve25519KeyForPublication clientKey;
	Curve25519KeyForPublication serverKey;
	
	// symmetric key - used for encryption
	SensitiveBytes key;
	
	SecureMessageOutputStream out;
	SecureMessageInputStream in;
	
	SecureRandom rand = new SecureRandom(); // needed to generate iv values

	
	//package-protected constructor, used only by server
	C2Socket(Curve25519KeyContents serverKey) throws IOException {
		super();
		this.serverKey=serverKey;
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public C2Socket(Curve25519KeyContents clientKey, InetAddress address, int port) throws IOException {
		super(address, port);
		this.clientKey=clientKey;
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public C2Socket(Curve25519KeyContents clientKey, String host, int port) throws UnknownHostException,
			IOException {
		super(host, port);
		this.clientKey=clientKey;
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		if(out == null) {
			log.trace("Attempting to initialize OutputStream");
			if(key == null) throw new RuntimeException("handshake not yet complete");
			out = new SecureMessageOutputStream(key, super.getOutputStream());
		}
		return out;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		if(in == null) {
			log.trace("Attempting to initialize InputStream");
			if(key == null) throw new RuntimeException("handshake not yet complete");
			in = new SecureMessageInputStream(key, super.getInputStream());
		}
		return in;
	}
	
	public boolean clientsHandshake() {
		log.trace("entering client handshake");
		lock.lock();
		try {
			try {
				
				OutputStream _out = super.getOutputStream();
				InputStream _in = super.getInputStream();
				
				// 1.0 send our client public key portion
				Curve25519KeyForPublication _public = ((Curve25519KeyContents)clientKey).forPublication();
				C2KeyForPublicationOutputFrame frame = new C2KeyForPublicationOutputFrame(_public);
				frame.writeFrame(_out);
				log.trace("sent our client public key portion");
				
				// 1.1 collect the server public key
				InputFrameReader reader = new InputFrameReader();
				serverKey = reader.readC2KeyContents(_in);
				log.trace("collected server public key portion");
				
				this.createClientKey();
				log.info("created client key: "+clientKey.getDistinguishedHandle());
				
				return true;
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}finally{
			lock.unlock();
		}
	}
	
    boolean serversHandshake() {
    	log.trace("started server handshake");
		lock.lock();
		try {
			try {
				
				OutputStream _out = super.getOutputStream();
				InputStream _in = super.getInputStream();
				
				// 1.0 get server public key
				InputFrameReader reader = new InputFrameReader();
				clientKey = reader.readC2KeyContents(_in);
				log.trace("got client public key portion");
				
				// 1.1 send our server public key portion
				Curve25519KeyForPublication _public = ((Curve25519KeyContents)serverKey).forPublication();
				C2KeyForPublicationOutputFrame frame = new C2KeyForPublicationOutputFrame(_public);
				frame.writeFrame(_out);
				log.trace("sent our server public key portion");
				
				this.createServerKey();
				log.info("created server key: "+serverKey.getDistinguishedHandle());
			
				return true;
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}finally{
			lock.unlock();
		}
	}

    
    protected void createClientKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(serverKey.publicKey, ((Curve25519KeyContents)clientKey).agreementPrivateKey);
    	key = new SensitiveBytes(sk.getSHA256Digest());
    }
    
    protected void createServerKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(clientKey.publicKey, ((Curve25519KeyContents)serverKey).agreementPrivateKey);
    	key = new SensitiveBytes(sk.getSHA256Digest());
    }

}
