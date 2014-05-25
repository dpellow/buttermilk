package com.cryptoregistry.rsa;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;


import x.org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import x.org.bouncycastle.crypto.params.RSAKeyGenerationParameters;

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
			RSAKeyContents contents = kpGen.generateKeys();
			return contents;
		} finally {
			lock.unlock();
		}
	}
	
	public void encrypt(byte[] bytes){
		
	}
}
