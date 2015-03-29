/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.rsa;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import x.org.bouncycastle.crypto.encodings.PKCS1Encoding;

import com.cryptoregistry.SignatureAlgorithm;
import com.cryptoregistry.signature.RSACryptoSignature;
import com.cryptoregistry.signature.RSASignature;
import com.cryptoregistry.signature.SignatureMetadata;

import x.org.bouncycastle.crypto.InvalidCipherTextException;
import x.org.bouncycastle.crypto.engines.RSABlindedEngine;

import x.org.bouncycastle.crypto.AsymmetricBlockCipher;
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
	
	/**
	 * Will set up for Mode.SEALED
	 * @param password
	 * @return
	 */
	public RSAKeyContents generateKeys(char [] password) {
		lock.lock();
		try {
			RSAKeyPairGenerator kpGen = new RSAKeyPairGenerator(password);
			kpGen.init(new RSAKeyGenerationParameters(PUBLIC_EXPONENT, rand, KEY_STRENGTH, CERTAINTY));
			return kpGen.generateKeys();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Will set up for Mode.OPEN
	 * @param password
	 * @return
	 */
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
	
	public RSAKeyContents generateKeys(int strength) {
		lock.lock();
		try {
			RSAKeyPairGenerator kpGen = new RSAKeyPairGenerator();
			kpGen.init(new RSAKeyGenerationParameters(PUBLIC_EXPONENT, rand, strength, CERTAINTY));
			return kpGen.generateKeys();
		} finally {
			lock.unlock();
		}
	}
	
	public RSAKeyContents generateKeys(RSAKeyMetadata metadata) {
		return generateKeys(metadata,0x11,KEY_STRENGTH,CERTAINTY);
	}
	
	/**
	 * Set most parameters yourself
	 * 
	 * @param metadata
	 * @param publicExp
	 * @param keyStrength
	 * @param certainty
	 * @return
	 */
	public RSAKeyContents generateKeys(RSAKeyMetadata metadata, int publicExp, int keyStrength, int certainty) {
		lock.lock();
		try {
			RSAKeyPairGenerator kpGen = new RSAKeyPairGenerator(metadata);
			kpGen.init(new RSAKeyGenerationParameters(BigInteger.valueOf(publicExp), rand, keyStrength, certainty));
			return kpGen.generateKeys();
		} finally {
			lock.unlock();
		}
	}
	
	public byte [] decrypt(RSAKeyContents key, RSAEngineFactory.Padding pad, byte[] in){
		lock.lock();
		try {
			return runEngineDecrypt(false,key, pad.toString(), in);
		} finally {
			lock.unlock();
		}
	}
	
	public byte [] encrypt(RSAKeyForPublication key, RSAEngineFactory.Padding pad, byte[] in){
		lock.lock();
		try {
			return runEngineEncrypt(true,key, pad.toString(), in);
		} finally {
			lock.unlock();
		}
	}
	
	private byte [] runEngineEncrypt(boolean forEnc, RSAKeyForPublication key, String paddingScheme, byte[] in){
		RSAKeyParameters params = key.getPublicKey();
		AsymmetricBlockCipher rsa = new RSAEngineFactory(paddingScheme).getCipher();
		rsa.init(forEnc, params);
		return engineDoFinal(in,0,in.length,rsa);
	}
	
	private byte [] runEngineDecrypt(boolean forEnc, RSAKeyContents key, String paddingScheme, byte[] in){
		RSAKeyParameters params = key.getPrivateKey();
		AsymmetricBlockCipher rsa = new RSAEngineFactory(paddingScheme).getCipher();
		rsa.init(forEnc, params);
		return engineDoFinal(in,0,in.length,rsa);
	}
	
	private byte[] engineDoFinal(
	        byte[]  input,
	        int     inputOffset,
	        int     inputLen,
	        AsymmetricBlockCipher cipher) {
		
		  ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		  
	        if (input != null)
	        {
	            bOut.write(input, inputOffset, inputLen);
	        }

	        if (cipher instanceof RSABlindedEngine)
	        {
	            if (bOut.size() > cipher.getInputBlockSize() + 1)
	            {
	                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
	            }
	        }
	        else
	        {
	            if (bOut.size() > cipher.getInputBlockSize())
	            {
	                throw new ArrayIndexOutOfBoundsException("too much data for RSA block");
	            }
	        }

	        try
	        {
	            byte[]  bytes = bOut.toByteArray();

	            bOut.reset();

	            return cipher.processBlock(bytes, 0, bytes.length);
	        }
	        catch (InvalidCipherTextException e)
	        {
	            throw new RuntimeException(e.getMessage());
	        }
	    }
	
	/**
	 * Normally you would not call this directly, use the RSASignatureBuilder
	 * 
	 * 
	 * 
	 * @param signedBy
	 * @param pKeys
	 * @param msgHashBytes
	 * 
	 * @see RSASignatureBuilder
	 * 
	 */
	public RSACryptoSignature sign(String signedBy, RSAKeyContents sKeys, String digestName, byte [] msgHashBytes){
		
		lock.lock();
		try {
			AsymmetricBlockCipher rsaEngine = new PKCS1Encoding(new RSABlindedEngine());
			rsaEngine.init(true, sKeys.getPrivateKey());
			byte [] sigBytes = rsaEngine.processBlock(msgHashBytes, 0, msgHashBytes.length);
			RSASignature sig = new RSASignature(sigBytes);
			SignatureMetadata meta = new SignatureMetadata(
					UUID.randomUUID().toString(),
					new Date(),
					SignatureAlgorithm.RSA,
					digestName,
					sKeys.getMetadata().getHandle(),
					signedBy);
			return new RSACryptoSignature(meta,sig);
		} finally {
			lock.unlock();
		}
	}
	
	public RSACryptoSignature sign(SignatureMetadata meta, RSAKeyContents sKeys, byte [] msgHashBytes){
		
		lock.lock();
		try {
			AsymmetricBlockCipher rsaEngine = new PKCS1Encoding(new RSABlindedEngine());
			rsaEngine.init(true, sKeys.getPrivateKey());
			byte [] sigBytes = rsaEngine.processBlock(msgHashBytes, 0, msgHashBytes.length);
			RSASignature sig = new RSASignature(sigBytes);
			return new RSACryptoSignature(meta,sig);
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Assume the caller is using SHA-256 digest to make msgHashBytes
	 * 
	 * @param sig
	 * @param pKey
	 * @param msgHashBytes
	 * @return
	 */
	public boolean verify(RSACryptoSignature sig, RSAKeyForPublication pKey, byte [] msgHashBytes){
		lock.lock();
		try {
			AsymmetricBlockCipher rsaEngine = new PKCS1Encoding(new RSABlindedEngine());
			rsaEngine.init(false, pKey.getPublicKey());
			byte [] sigBytes = sig.signature.s.decodeToBytes();
			byte [] rawBytes = rsaEngine.processBlock(sigBytes, 0, sigBytes.length);
			return test_equal(rawBytes,msgHashBytes);
		} finally {
			lock.unlock();
		}
	}
	
	private boolean test_equal(byte[] a, byte[] b) {
		int i;
		for (i = 0; i < 32; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
	
}
