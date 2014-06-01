/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;


import x.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import x.org.bouncycastle.crypto.agreement.ECDHBasicAgreement;
import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import x.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import x.org.bouncycastle.crypto.params.ECPublicKeyParameters;

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

	public ECKeyContents generateKeys(final String curveName) {
		lock.lock();
		try {
			ECKeyPairGenerator gen = new ECKeyPairGenerator();
			ECDomainParameters domainParams = CurveFactory.getCurveForName(curveName);
			ECKeyGenerationParameters params = new ECKeyGenerationParameters(domainParams,rand);
			gen.init(params);
			AsymmetricCipherKeyPair pair = gen.generateKeyPair();
			ECPrivateKeyParameters priv = (ECPrivateKeyParameters) pair.getPrivate();
			ECPublicKeyParameters pub = (ECPublicKeyParameters) pair.getPublic();
			return new ECKeyContents(pub.getQ(),priv.getParameters().getName(),priv.getD());
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Use for Mode.SEALED key setup
	 * @param password
	 * @param curveName
	 * @return
	 */
	public ECKeyContents generateKeys(char [] password, final String curveName) {
		lock.lock();
		try {
			ECKeyPairGenerator gen = new ECKeyPairGenerator();
			ECDomainParameters domainParams = CurveFactory.getCurveForName(curveName);
			ECKeyGenerationParameters params = new ECKeyGenerationParameters(domainParams,rand);
			gen.init(params);
			AsymmetricCipherKeyPair pair = gen.generateKeyPair();
			ECPrivateKeyParameters priv = (ECPrivateKeyParameters) pair.getPrivate();
			ECPublicKeyParameters pub = (ECPublicKeyParameters) pair.getPublic();
			return new ECKeyContents(password,pub.getQ(),priv.getParameters().getName(),priv.getD());
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Does EC Diffie-Hellman key agreement. Returns a 256 bit hash of the result suitable for use
	 * as an encryption key
	 * 
	 * @param ours
	 * @param theirs
	 * @return
	 */
	public byte [] keyAgreement(ECKeyContents ours, ECKeyForPublication theirs){
		lock.lock();
		try {
			ECDHBasicAgreement agree = new ECDHBasicAgreement();
			agree.init(ours.getPrivateKey());
			BigInteger bi = agree.calculateAgreement(theirs.getPublicKey());
			SHA256Digest digest = new SHA256Digest();
			byte [] bytes = bi.toByteArray();
			digest.update(bytes, 0, bytes.length);
			byte[]  digestBytes = new byte[digest.getDigestSize()];
		    digest.doFinal(digestBytes, 0);
		    return digestBytes;
		} finally {
			lock.unlock();
		}
	}
	
}
