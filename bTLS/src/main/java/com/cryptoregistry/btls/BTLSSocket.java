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

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.crypto.mt.Encryptor;
import com.cryptoregistry.crypto.mt.Segment;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.builder.*;
import com.cryptoregistry.proto.reader.HelloAckProtoReader;
import com.cryptoregistry.proto.reader.HelloProtoReader;
import com.cryptoregistry.protos.Buttermilk.HelloAckProto;
import com.cryptoregistry.protos.Buttermilk.HelloProto;
import com.cryptoregistry.protos.Buttermilk.SegmentProto;

/**
 * A secure socket
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
	
	SegmentOutputStream out;
	SegmentInputStream in;
	
	byte[] randBytes; // validation bytes
	
	SecureRandom rand = new SecureRandom();
	
	//package-protected, do not call directly
	BTLSSocket() throws IOException {
		super();
		clientKey = Configuration.CONFIG.clientKey();
	}
	
	//package-protected, used only by server
	BTLSSocket(Curve25519KeyContents serverKey, byte[] bytes) throws IOException {
		super();
		this.serverKey=serverKey;
		this.randBytes=bytes;
	}

	/**
	 * Client can call
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
	 * Client can call
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
	
	protected void initStreams() throws IOException {
		out = new SegmentOutputStream(getOutputStream());
		in = new SegmentInputStream(getInputStream());
	}

	/**
	 * Must be called by client prior to sending any data
	 * 
	 * @return
	 */
	public boolean clientsHandshake() {
		lock.lock();
		try {
			try {
				
				// 1.0 send our client public key
				HelloProtoBuilder builder = new HelloProtoBuilder(clientKey);
				HelloProto hproto = builder.build();
				out.write(hproto.toByteString().toByteArray());
				out.flush();
				
				// 1.1 get the server public key
				HelloAckProtoReader reader = new HelloAckProtoReader(HelloAckProto.parseFrom(in));
				serverKey = (Curve25519KeyContents) reader.read();
				this.randBytes = reader.getRand32();
				
				createClientKey();
				Segment seg = clientEncryptValidationBytes();
				SegmentProto sp = new SegmentProtoBuilder(seg).build();
				out.write(sp.toByteString().toByteArray());
				out.flush();
				
			
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}finally{
			lock.unlock();
		}
		
		return true;
	}
	
    boolean serversHandshake() {
		
		lock.lock();
		try {
			try {
				// 1.0 get client key
				OutputStream out = getOutputStream();
				InputStream input = getInputStream();
				HelloProtoReader reader = new HelloProtoReader(HelloProto.parseFrom(input));
				clientKey = (Curve25519KeyContents) reader.read();
				
				// 1.1 send our key and some rand bytes
				HelloAckProtoBuilder builder = new HelloAckProtoBuilder(serverKey,randBytes);
				HelloAckProto hproto = builder.build();
				out.write(hproto.toByteString().toByteArray());
				out.flush();
				
				createServerKey();
			
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}finally{
			lock.unlock();
		}
		
		return false;
	}
    
    private void createClientKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(serverKey.publicKey, clientKey.agreementPrivateKey);
    	key = new SensitiveBytes(sk.getSHA256Digest());
    }
    
    private void createServerKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(clientKey.publicKey, serverKey.agreementPrivateKey);
    	key = new SensitiveBytes(sk.getSHA256Digest());
    }
	
    private Segment clientEncryptValidationBytes() {
    	// make IV
    	byte [] iv = new byte[16];
		rand.nextBytes(iv);
		Segment seg0 = new Segment(randBytes);
		Encryptor enc = new Encryptor(key.getData(),iv,seg0);
		try {
			enc.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return seg0;
    }

}
