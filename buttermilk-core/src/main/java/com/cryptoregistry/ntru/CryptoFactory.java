/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ntru;

import x.org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionKeyGenerationParameters;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionKeyPairGenerator;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionPrivateKeyParameters;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionPublicKeyParameters;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEngine;

public class CryptoFactory {
	
	public static final CryptoFactory INSTANCE = new CryptoFactory();

	private CryptoFactory() {
		
	}
	
	/**
	 * A reasonable default choice for parameters
	 */
	public NTRUKeyContents generateKeys() {
		NTRUEncryptionKeyPairGenerator gen = new NTRUEncryptionKeyPairGenerator();
		gen.init(NTRUEncryptionKeyGenerationParameters.EES1087EP2);
		AsymmetricCipherKeyPair pair = gen.generateKeyPair();
		NTRUEncryptionPublicKeyParameters pub  = (NTRUEncryptionPublicKeyParameters) pair.getPublic();
		NTRUEncryptionPrivateKeyParameters priv =  (NTRUEncryptionPrivateKeyParameters) pair.getPrivate();
		return new NTRUKeyContents(NTRUNamedParameters.EES1087EP2,pub.h,priv.t,priv.fp);
	}
	
	public NTRUKeyContents generateKeys(NTRUNamedParameters paramName) {
		NTRUEncryptionKeyPairGenerator gen = new NTRUEncryptionKeyPairGenerator();
		gen.init(NTRUEncryptionKeyGenerationParameters.EES1087EP2);
		AsymmetricCipherKeyPair pair = gen.generateKeyPair();
		NTRUEncryptionPublicKeyParameters pub  = (NTRUEncryptionPublicKeyParameters) pair.getPublic();
		NTRUEncryptionPrivateKeyParameters priv =  (NTRUEncryptionPrivateKeyParameters) pair.getPrivate();
		return new NTRUKeyContents(paramName,pub.h,priv.t,priv.fp);
	}
	
	public NTRUKeyContents generateKeys(NTRUKeyMetadata metadata) {
		NTRUEncryptionKeyPairGenerator gen = new NTRUEncryptionKeyPairGenerator();
		gen.init(NTRUEncryptionKeyGenerationParameters.EES1087EP2);
		AsymmetricCipherKeyPair pair = gen.generateKeyPair();
		NTRUEncryptionPublicKeyParameters pub  = (NTRUEncryptionPublicKeyParameters) pair.getPublic();
		NTRUEncryptionPrivateKeyParameters priv =  (NTRUEncryptionPrivateKeyParameters) pair.getPrivate();
		return new NTRUKeyContents(metadata,NTRUNamedParameters.EES1087EP2,pub.h,priv.t,priv.fp);
	}
	
	public NTRUKeyContents generateKeys(NTRUEncryptionKeyGenerationParameters params) {
		NTRUEncryptionKeyPairGenerator gen = new NTRUEncryptionKeyPairGenerator();
		gen.init(params);
		AsymmetricCipherKeyPair pair = gen.generateKeyPair();
		NTRUEncryptionPublicKeyParameters pub  = (NTRUEncryptionPublicKeyParameters) pair.getPublic();
		NTRUEncryptionPrivateKeyParameters priv =  (NTRUEncryptionPrivateKeyParameters) pair.getPrivate();
		return new NTRUKeyContents(NTRUKeyMetadata.createDefault(),pub.getParameters(),pub.h,priv.t,priv.fp);
	}
	
	public NTRUKeyContents generateKeys(NTRUKeyMetadata metadata, NTRUEncryptionKeyGenerationParameters params) {
		NTRUEncryptionKeyPairGenerator gen = new NTRUEncryptionKeyPairGenerator();
		gen.init(params);
		AsymmetricCipherKeyPair pair = gen.generateKeyPair();
		NTRUEncryptionPublicKeyParameters pub  = (NTRUEncryptionPublicKeyParameters) pair.getPublic();
		NTRUEncryptionPrivateKeyParameters priv =  (NTRUEncryptionPrivateKeyParameters) pair.getPrivate();
		return new NTRUKeyContents(metadata,pub.getParameters(),pub.h,priv.t,priv.fp);
	}
	
	public NTRUKeyContents generateKeys(NTRUKeyMetadata metadata, NTRUNamedParameters e) {
		NTRUEncryptionKeyPairGenerator gen = new NTRUEncryptionKeyPairGenerator();
		gen.init(e.getKeyGenerationParameters());
		AsymmetricCipherKeyPair pair = gen.generateKeyPair();
		NTRUEncryptionPublicKeyParameters pub  = (NTRUEncryptionPublicKeyParameters) pair.getPublic();
		NTRUEncryptionPrivateKeyParameters priv =  (NTRUEncryptionPrivateKeyParameters) pair.getPrivate();
		return new NTRUKeyContents(metadata,e,pub.h,priv.t,priv.fp);
	}
	
	public byte [] encrypt(NTRUKeyForPublication pKey, byte [] in){
		
		NTRUEngine engine = new NTRUEngine();
		engine.init(true, pKey.getPublicKey());
		if(in.length > pKey.params.maxMsgLenBytes)
			throw new RuntimeException("Max size we can work with = "+ pKey.params.maxMsgLenBytes);
	    return engine.processBlock(in, 0, in.length);
		
	}
	
	public byte [] decrypt(NTRUKeyContents sKey, byte [] encrypted){
		
		NTRUEngine engine = new NTRUEngine();
		engine.init(false, sKey.getPrivateKey());
	    return engine.processBlock(encrypted, 0, encrypted.length);
		
	}

}
