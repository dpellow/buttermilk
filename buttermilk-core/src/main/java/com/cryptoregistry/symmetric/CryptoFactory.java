/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */package com.cryptoregistry.symmetric;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.passwords.Password;

/**
 * The symmetric CryptoFactory allows for the creation of protected keys for use
 * with AES, etc.
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

	public SymmetricKeyContents generateKey(Password password, int size) {
		lock.lock();
		try {
			if (size < 1) throw new RuntimeException("Size looks wrong.");
			if (!(size == 128 || size == 192 || size == 256)) throw new RuntimeException("Size looks wrong, set to 128, 192, or 256 bits");
			byte[] bytes = new byte[size/8];
			rand.nextBytes(bytes);
			return new SymmetricKeyContents(
					SymmetricKeyMetadata.createSecureDefault(password
							.getPassword()), bytes);
		} finally {
			lock.unlock();
		}
	}
	
	public SymmetricKeyContents generateKey(int size) {
		lock.lock();
		try {
			if (size < 1) throw new RuntimeException("Size looks wrong.");
			if (!(size == 128 || size == 192 || size == 256)) throw new RuntimeException("Size looks wrong.");
			byte[] bytes = new byte[size/8];
			rand.nextBytes(bytes);
			return new SymmetricKeyContents(SymmetricKeyMetadata.createUnsecure(), bytes);
		} finally {
			lock.unlock();
		}
	}
	
	public SymmetricKeyContents wrap(Password password, byte[]bytes) {
		lock.lock();
		try {
			return new SymmetricKeyContents(
					SymmetricKeyMetadata.createSecureDefault(password
							.getPassword()), bytes);
		} finally {
			lock.unlock();
		}
	}

}
