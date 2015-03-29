/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUNamedParameters;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.symmetric.SymmetricKeyContents;

/**
 * Master Factory for crypto operations. This is just syntatic sugar
 * 
 * @author Dave
 *
 */
public class Buttermilk {
	
	public static final Buttermilk INSTANCE = new Buttermilk();
	
	private Buttermilk() {}
	
	public final SymmetricKeyContents generateSymmetricKey() {
		return com.cryptoregistry.symmetric.CryptoFactory.INSTANCE.generateKey(256);
	}
	
	public final SymmetricKeyContents generateSymmetricKey(int sz) {
		return com.cryptoregistry.symmetric.CryptoFactory.INSTANCE.generateKey(sz);
	}
	
	public final SymmetricKeyContents generateSymmetricKey(Password password, int sz) {
		return com.cryptoregistry.symmetric.CryptoFactory.INSTANCE.generateKey(password, sz);
	}
	
	public final Curve25519KeyContents generateC2Keys() {
		return com.cryptoregistry.c2.CryptoFactory.INSTANCE.generateKeys();
	}
	
	public final Curve25519KeyContents generateC2Keys(C2KeyMetadata meta) {
		return com.cryptoregistry.c2.CryptoFactory.INSTANCE.generateKeys(meta);
	}
	
	public final ECKeyContents generateECKeys() {
		return com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys("P-256");
	}
	
	public final ECKeyContents generateECKeys(String curveName) {
		return com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys(curveName);
	}
	
	public final ECKeyContents generateECKeys(char [] password, String curveName) {
		return com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys(password, curveName);
	}
	
	public final ECKeyContents generateECKeys(ECKeyMetadata meta, String curveName) {
		return com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys(meta, curveName);
	}
	
	public final ECKeyContents generateECKeys(ECCustomParameters domainParams) {
		return com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateCustomKeys(domainParams);
	}
	
	public final RSAKeyContents generateRSAKeys() {
		return com.cryptoregistry.rsa.CryptoFactory.INSTANCE.generateKeys();
	}
	
	public final RSAKeyContents generateRSAKeys(int keysize) {
		if(keysize%1024 != 0) throw new RuntimeException("Bad key size: "+keysize);
		return com.cryptoregistry.rsa.CryptoFactory.INSTANCE.generateKeys(keysize);
	}
	
	public final RSAKeyContents generateRSAKeys(RSAKeyMetadata meta) {
		return com.cryptoregistry.rsa.CryptoFactory.INSTANCE.generateKeys(meta);
	}
	
	public final NTRUKeyContents generateNTRUKeys(){
		return com.cryptoregistry.ntru.CryptoFactory.INSTANCE.generateKeys();
	}
	
	public final NTRUKeyContents generateNTRUKeys(NTRUNamedParameters paramName){
		return com.cryptoregistry.ntru.CryptoFactory.INSTANCE.generateKeys(paramName);
	}

}
