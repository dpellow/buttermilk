package com.cryptoregistry.symmetric;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

/**
 * The symmetric CryptoFactory allows for the creation of protected keys for use with AES, etc.
 * 
 * @author Dave
 *
 */
public class CryptoFactory {

	private final ReentrantLock lock;
	private final SecureRandom rand;
	
	public static final CryptoFactory INSTANCE = new CryptoFactory();

	private CryptoFactory() {
		lock = new ReentrantLock();
		try {
			rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public SymmetricKeyContents generateKey(int size, Password password){
		if(size<1) throw new RuntimeException("Size looks wrong.");
		byte [] bytes = new byte[size];
		rand.nextBytes(bytes);
		
		return null;
	}

}
