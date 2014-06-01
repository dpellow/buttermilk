/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.c2;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.c2.key.AgreementPrivateKey;
import com.cryptoregistry.c2.key.C2KeyManagement;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.c2.key.SigningPrivateKey;

/**
 * <pre>
 * CryptoFactory provides some of the classes one would expect when doing cryptography, such as
 * PrivateKey, PublicKey, and so on. These classes do not implement the javax.crypto interfaces.
 * Example use for ECDH:
 * 
   Curve25519KeyContents keys0 = CryptoFactory.INSTANCE.generateKeys();
   Curve25519KeyContents keys1 = CryptoFactory.INSTANCE.generateKeys();
   SecretKey s0 = CryptoFactory.INSTANCE.keyAgreement(keys1.publicKey, keys0.agreementPrivateKey);
   SecretKey s1 = CryptoFactory.INSTANCE.keyAgreement(keys0.publicKey, keys1.agreementPrivateKey);
   Assert.assertTrue(test_equal(s0.getBytes(),s1.getBytes()));
   s0.selfDestruct();
   s1.selfDestruct(); // remove the key bytes from the heap memory after use
		
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public class CryptoFactory {

	private final Curve25519 curve;
	private final ReentrantLock lock;
	private final SecureRandom rand;
	
	public static final CryptoFactory INSTANCE = new CryptoFactory();
	
	private CryptoFactory() {
		curve = new Curve25519();
		lock = new ReentrantLock();
		try {
			rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Curve25519KeyContents generateKeys(){
		lock.lock();
		try {
			byte[]P=new byte[32];
			byte[]s=new byte[32];
			byte[]k=new byte[32];
			rand.nextBytes(k);
			curve.keygen(P, s, k);
			return new Curve25519KeyContents(new PublicKey(P),new SigningPrivateKey(s),new AgreementPrivateKey(k));
		}finally{
			lock.unlock();
		}
	}
	
	public Curve25519KeyContents generateKeys(C2KeyManagement management){
		lock.lock();
		try {
			byte[]P=new byte[32];
			byte[]s=new byte[32];
			byte[]k=new byte[32];
			rand.nextBytes(k);
			curve.keygen(P, s, k);
			return new Curve25519KeyContents(management, new PublicKey(P),new SigningPrivateKey(s),new AgreementPrivateKey(k));
		}finally{
			lock.unlock();
		}
	}
	
	public SecretKey keyAgreement(PublicKey peerPublicKey, AgreementPrivateKey privKey){
		lock.lock();
		try {
			byte [] Z = new byte[32];
			curve.curve(Z, privKey.getBytes(), peerPublicKey.getBytes());
			return new SecretKey(Z);
		}finally{
			lock.unlock();
		}
	}

}
