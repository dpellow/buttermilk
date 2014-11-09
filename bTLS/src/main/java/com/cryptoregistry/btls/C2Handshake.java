package com.cryptoregistry.btls;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import x.org.bouncycastle.crypto.engines.AESFastEngine;
import x.org.bouncycastle.crypto.io.CipherInputStream;
import x.org.bouncycastle.crypto.io.CipherOutputStream;
import x.org.bouncycastle.crypto.modes.CBCBlockCipher;
import x.org.bouncycastle.crypto.paddings.PKCS7Padding;
import x.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import x.org.bouncycastle.crypto.params.KeyParameter;
import x.org.bouncycastle.crypto.params.ParametersWithIV;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.frame.BytesOutputFrame;
import com.cryptoregistry.proto.frame.C2KeyForPublicationOutputFrame;
import com.cryptoregistry.proto.frame.InputFrameReader;
import com.cryptoregistry.symmetric.AESCBCPKCS7;

/**
Protocol: 
	 
	 1a) Client initiates connection and send public key
	 
	 	A) sends C2KeyForPublicationOutputFrame
	 	
	 1b) Server receives public key, performs ECDH and builds an encryption key, [k, iv] where iv is 16 random bytes 
	 
	 	A) receive with InputFrameReader.readC2KeyContents()
	 	B) perform ecdh to create agreement key [k], sha-256 on result gets [k's]
	 	C) generate 256 random bytes, encrypt those with [k's] and iv to make our secret key [z]
	 
	 2a) Server respond with her own public key and encrypted ephemeral key
	 
	 	A) sends C2KeyForPublicationOutputFrame 
	 	B) sends BytesOutputFrame with [z] for the encrypted key
	 	C) sends BytesOutputFrame for the iv
	 	
	 
	 2a) Client receives public key from server and the iv, performs ECDH and builds encryption key [k's], decrypts z with it.
	 
	 	A) receive with InputFrameReader.readC2KeyContents()
	 	A) receive encrypted key with InputFrameReader.readByteProto()
	 	A) receive raw iv with InputFrameReader.readByteProto()
		B) perform ecdh to create agreement key [k], sha-256 on result gets [k's]
		C) decrypt to get our ephemeral secret key [z]
	 
	 
	 3) communications now begin over the streams using [z, iv]. 
	 
*/
	 
public class C2Handshake implements Handshake {
	
		private static final Logger log = Logger.getLogger("com.cryptography.btls.C2Handshake");
		
		Lock lock = new ReentrantLock();
	
		// asymmetric keys used in handshake. 
		Curve25519KeyForPublication clientKey;
		Curve25519KeyForPublication serverKey;
		
		// symmetric key - used for encryption in the Cipher streams
		SensitiveBytes key;
		byte [] iv;
		
		SecureRandom rand = new SecureRandom();
		
		// parent I/O from the Socket, we decorate these
		InputStream input;
		OutputStream output;


	public C2Handshake(boolean isServer, Curve25519KeyContents key, InputStream input, OutputStream output) {
		if(key == null) throw new RuntimeException("Key required, cannnot be null");
		if(isServer) {
			serverKey = key;
		}else{
			clientKey = key;
		}
		
		this.input = input;
		this.output = output;
	}
	
	/* (non-Javadoc)
	 * @see com.cryptoregistry.btls.Handshake#clientsHandshake()
	 */
	@Override
	public boolean clientsHandshake() {
		log.trace("started client's handshake");
		lock.lock();
		try {
				
				C2KeyForPublicationOutputFrame frame = null;
				
				// 1.0 send our client public key portion
				frame = new C2KeyForPublicationOutputFrame(clientKey);
				frame.writeFrame(output);
				log.trace("sent our client public key portion");
				
				// 1.1 collect the server public key
				InputFrameReader reader = new InputFrameReader();
				serverKey = reader.readC2KeyContents(input);
				log.trace("collected server public key portion");
				
				// 1.2 collect the encrypted bytes of the secret key
				reader = new InputFrameReader();
				byte [] encBytes = reader.readBytesProto(input);
				log.trace("collected encrypted key from server");
				
				// 1.3 collect the raw iv
				reader = new InputFrameReader();
				iv = reader.readBytesProto(input);
				log.trace("collected raw iv");
				
				// 1.4 decrypt key
				this.createClientKey(encBytes);
				log.info("completed protocol handshake");
				
				return true;
			
		}finally{
			lock.unlock();
		}
	}
	
    /* (non-Javadoc)
	 * @see com.cryptoregistry.btls.Handshake#serversHandshake()
	 */
    @Override
	public boolean serversHandshake() {
    	log.trace("started server's handshake");
		lock.lock();
		try {
				
				// 1.0 get client's public key
				InputFrameReader reader = new InputFrameReader();
				clientKey = reader.readC2KeyContents(input);
				log.trace("got client public key portion");
				
				// 1.1 send our server public key portion
				Curve25519KeyForPublication _public = ((Curve25519KeyContents)serverKey).forPublication();
				C2KeyForPublicationOutputFrame frame = new C2KeyForPublicationOutputFrame(_public);
				frame.writeFrame(output);
				log.trace("sent our server public key portion");
				
				// 1.2 send encrypted key
				BytesOutputFrame bframe = new BytesOutputFrame(createServerKey());
				bframe.writeFrame(output);
				log.info("sent encrypted key: ");
				
				// 1.3 send iv
				bframe = new BytesOutputFrame(iv);
				bframe.writeFrame(output);
				log.info("sent iv");
			
				return true;
			
		}finally{
			lock.unlock();
		}
	}

    
    protected void createClientKey(byte [] encrypted){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(serverKey.publicKey, 
    			((Curve25519KeyContents)clientKey).agreementPrivateKey);
    	byte [] agreementKey = sk.getSHA256Digest();
    	AESCBCPKCS7 aes = new AESCBCPKCS7(agreementKey,iv); 
    	key = new SensitiveBytes(aes.decrypt(encrypted));
    }
    
    protected byte[] createServerKey(){
    	SecretKey sk = CryptoFactory.INSTANCE.keyAgreement(clientKey.publicKey, 
    			((Curve25519KeyContents)serverKey).agreementPrivateKey);
    	byte [] agreementKey = sk.getSHA256Digest();
    	byte [] _key = new byte[32];
		iv = new byte[16];
		rand.nextBytes(_key);
		rand.nextBytes(iv);
		key = new SensitiveBytes(_key);
		
		AESCBCPKCS7 aes = new AESCBCPKCS7(agreementKey,iv);
		return aes.encrypt(_key);
    }
    
	public ParametersWithIV buildParameters() {
		ParametersWithIV holder = new ParametersWithIV(
				new KeyParameter(key.getData(), 0, key.getData().length), 
				iv, 
				0, 
				iv.length);
		return holder;
	}

	@Override
	public CipherOutputStream decorateOutputStream() {
		ParametersWithIV holder = this.buildParameters();
		CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
		PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
		aesCipher.init(true, holder);
		return new CipherOutputStream(output,aesCipher);
	}

	@Override
	public CipherInputStream decorateInputStream() {
		ParametersWithIV holder = this.buildParameters();
		CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
		PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
		aesCipher.init(false, holder);
		return new CipherInputStream(input,aesCipher);
	}
}
