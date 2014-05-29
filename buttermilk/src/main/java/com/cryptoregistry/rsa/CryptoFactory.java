package com.cryptoregistry.rsa;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.locks.ReentrantLock;

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
}
