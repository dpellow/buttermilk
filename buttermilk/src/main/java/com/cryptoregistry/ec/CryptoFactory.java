package com.cryptoregistry.ec;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;


import x.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
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
	
	public void encrypt(byte[] bytes){
		
	}
}
