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

import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.crypto.mt.Decryptor;
import com.cryptoregistry.crypto.mt.Encryptor;
import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.builder.*;
import com.cryptoregistry.proto.reader.HelloAckProtoReader;
import com.cryptoregistry.proto.reader.HelloProtoReader;
import com.cryptoregistry.proto.reader.SegmentProtoReader;
import com.cryptoregistry.protos.Buttermilk.HelloAckProto;
import com.cryptoregistry.protos.Buttermilk.HelloProto;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;
import com.cryptoregistry.protos.Buttermilk.ServerReadyProto;

/**
 * A secure socket using contemporary techniques
 * 
 * @author Dave
 *
 */
public class BTLSSocket extends Socket {
	
	Lock lock = new ReentrantLock();
	
	// asymmetric keys used in handshake
	Curve25519KeyContents clientKey;
	Curve25519KeyContents serverKey;
	
	// symmetric key
	SensitiveBytes key;
	
	SecureMessageOutputStream out;
	SecureMessageInputStream in;
	
	byte[] randBytes; // validation bytes sent by sever and encrypted by client as a validation
	
	SecureRandom rand = new SecureRandom(); // needed to generate iv values
	
	//package-protected constructor, do not call directly
	BTLSSocket() throws IOException {
		super();
		clientKey = Configuration.CONFIG.clientKey();
	}
	
	//package-protected constructor, used only by server
	BTLSSocket(Curve25519KeyContents serverKey, byte[] bytes) throws IOException {
		super();
		this.serverKey=serverKey;
		this.randBytes=bytes;
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public BTLSSocket(InetAddress address, int port) throws IOException {
		super(address, port);
		clientKey = Configuration.CONFIG.clientKey();
	}

	/**
	 * Client can call this constructor
	 * 
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public BTLSSocket(String host, int port) throws UnknownHostException,
			IOException {
		super(host, port);
		clientKey = Configuration.CONFIG.clientKey();
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		if(out == null) {
			if(key == null) throw new RuntimeException("handshake not yet complete");
			out = new SecureMessageOutputStream(key, super.getOutputStream());
		}
		return out;
	}
	
	@Override
	public InputStream getInputStream() throws IOException {
		if(in == null) {
			if(key == null) throw new RuntimeException("handshake not yet complete");
			in = new SecureMessageInputStream(key, super.getInputStream());
		}
		return in;
	}

	/**
	 * Must be called by client prior to sending any data; thread-safe
	 * 
	 * @return
	 */
	public boolean clientsHandshake() {
		lock.lock();
		try {
			try {
				
				OutputStream _out = super.getOutputStream();
				InputStream _in = super.getInputStream();
				
				// 1.0 send our client public key
				HelloProtoBuilder builder = new HelloProtoBuilder(clientKey);
				HelloProto hproto = builder.build();
				_out.write(hproto.toByteString().toByteArray());
				_out.flush();
				
				// 1.1 get the server public key
				HelloAckProtoReader reader = new HelloAckProtoReader(HelloAckProto.parseFrom(_in));
				serverKey = (Curve25519KeyContents) reader.read();
				this.randBytes = reader.getRand32();
				
				// 1.2 create a small encrypted message segment and sent it back
				createClientKey();
				Segment seg = clientEncryptValidationBytes();
				SegmentProto sp = new SegmentProtoBuilder(seg).build();
				_out.write(sp.toByteString().toByteArray());
				_out.flush();
				
				// 1.3 Server will check if it can decrypt - if so, returns true
				ServerReadyProto ready = ServerReadyProto.parseFrom(_in);
				return ready.getReady();
			
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Called internally by BTLSServerSocket within the accept() method
	 * 
	 * @return
	 */
    boolean serversHandshake() {
		lock.lock();
		try {
			try {
				// 1.0 get client key
				OutputStream _out = super.getOutputStream();
				InputStream _in = super.getInputStream();
				HelloProtoReader reader = new HelloProtoReader(HelloProto.parseFrom(_in));
				clientKey = (Curve25519KeyContents) reader.read();
				
				// 1.1 send our key and some rand bytes
				HelloAckProtoBuilder builder = new HelloAckProtoBuilder(serverKey,randBytes);
				HelloAckProto hproto = builder.build();
				_out.write(hproto.toByteString().toByteArray());
				_out.flush();
				
				createServerKey();
				SegmentProtoReader sreader = new SegmentProtoReader(SegmentProto.parseFrom(_in));
				Segment segment = sreader.read();
				boolean OK = serverCheckSegment(segment);
				
				ServerReadyProto ready = null;
				if(OK){
					ready = ServerReadyProto.newBuilder().setReady(true).build(); 
				}else{
					ready = ServerReadyProto.newBuilder().setReady(false).build(); 
				}
				
				_out.write(ready.toByteString().toByteArray());
				_out.flush();
				
				return OK;
			
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}finally{
			lock.unlock();
		}
	}
    
    protected void createClientKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(serverKey.publicKey, clientKey.agreementPrivateKey);
    	key = new SensitiveBytes(sk.getSHA256Digest());
    }
    
    protected void createServerKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(clientKey.publicKey, serverKey.agreementPrivateKey);
    	key = new SensitiveBytes(sk.getSHA256Digest());
    }
	
    protected Segment clientEncryptValidationBytes() {
    	// make IV
    	byte [] iv = new byte[16];
		rand.nextBytes(iv);
		Segment seg0 = new Segment(randBytes,iv);
		Encryptor enc = new Encryptor(key.getData(),iv,seg0);
		try {
			enc.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return seg0;
    }
    
    protected boolean serverCheckSegment(Segment seg) {
    	Decryptor dec = new Decryptor(key.getData(),seg.getIv(),seg);
    	try {
			dec.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    	return Arrays.areEqual(randBytes,seg.getOutput());
    }

}
