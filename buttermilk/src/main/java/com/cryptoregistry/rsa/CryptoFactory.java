package com.cryptoregistry.rsa;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;


import x.org.bouncycastle.crypto.encodings.ISO9796d1Encoding;
import x.org.bouncycastle.crypto.encodings.OAEPEncoding;
import x.org.bouncycastle.crypto.encodings.PKCS1Encoding;
import x.org.bouncycastle.crypto.engines.RSAEngine;
import x.org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import x.org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import x.org.bouncycastle.crypto.params.RSAKeyParameters;

public class CryptoFactory {

	private final ReentrantLock lock;
	private final SecureRandom rand;

	public static final int CERTAINTY = 100;
	public static final int KEY_STRENGTH = 2048;

	// typically would be 3, 5, 17, 257, or 65537
	public static final BigInteger PUBLIC_EXPONENT = BigInteger.valueOf(0x11);

	public static final CryptoFactory INSTANCE = new CryptoFactory();

	private CryptoFactory() {
		lock = new ReentrantLock();
		try {
			rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public RSAKeyContents generateKeys() {
		lock.lock();
		try {
			RSAKeyPairGenerator kpGen = new RSAKeyPairGenerator();
			kpGen.init(new RSAKeyGenerationParameters(PUBLIC_EXPONENT, rand, KEY_STRENGTH, CERTAINTY));
			return kpGen.generateKeys();
		} finally {
			lock.unlock();
		}
	}
	
	public byte [] decrypt(RSAKeyContents key,Encoding enc, byte encryptedBytes){
		
		return null;
	}
	
	/**
	 * RSA encryption is only really viable for encapsulating another key, for example an AES key. 
	 * That's why this method does not allow encryption of anything larger than the size of the key; 
	 * e.g., 2048 bits.
	 * 
	 * @param bytes
	 */
	public byte [] encrypt(RSAKeyForPublication key, Encoding enc, byte[] in){
		
		lock.lock();
		try {
		
			if((in.length * 8) > KEY_STRENGTH) 
				throw new RuntimeException("data too large, bits must be less than "+KEY_STRENGTH);
			
			RSAKeyParameters params = key.getPublicKey();
			RSAEngine           rsa = new RSAEngine();

			switch(enc){
				case ISO9796d1: {
					ISO9796d1Encoding eng = new ISO9796d1Encoding(rsa);
			        eng.init(false, params);
			        return eng.processBlock(in, 0, in.length);
				}
				case OAEP: {
					OAEPEncoding eng = new OAEPEncoding(rsa);
					eng.init(false, params);
				    return eng.processBlock(in, 0, in.length);
				}
				case PKCS1: {
					PKCS1Encoding eng = new PKCS1Encoding(rsa);
					eng.init(false, params);
				    return eng.processBlock(in, 0, in.length);
				}
				default: throw new RuntimeException("Sorry, encoding scheme unknown: "+enc);
			}
			
			
		} finally {
			lock.unlock();
		}
	}
}
