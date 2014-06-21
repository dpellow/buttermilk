/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.rsa;

import java.math.BigInteger;

import com.cryptoregistry.Signer;

import x.org.bouncycastle.crypto.params.RSAKeyParameters;
import x.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

/**
 * Output from Key Generation for RSA
 * 
 * @author Dave
 *
 */
public class RSAKeyContents extends RSAKeyForPublication implements Signer {

	public final BigInteger  privateExponent;
	public final BigInteger  p;
	public final BigInteger  q;
	public final BigInteger  dP;
	public final BigInteger  dQ;
	public final BigInteger  qInv;
	
	/**
	 * Use for Mode.Open
	 * 
	 * @param modulus
	 * @param publicExponent
	 * @param privateExponent
	 * @param p
	 * @param q
	 * @param dP
	 * @param dQ
	 * @param qInv
	 */
	public RSAKeyContents(BigInteger modulus, BigInteger publicExponent,
			BigInteger privateExponent, BigInteger p, BigInteger q,
			BigInteger dP, BigInteger dQ, BigInteger qInv) {
		
		super(RSAKeyMetadata.createDefault(), modulus, publicExponent);
		
		this.privateExponent = privateExponent;
		this.p = p;
		this.q = q;
		this.dP = dP;
		this.dQ = dQ;
		this.qInv = qInv;
	}
	
	/**
	 * Use for Mode.SEALED 
	 * 
	 * @param password
	 * @param modulus
	 * @param publicExponent
	 * @param privateExponent
	 * @param p
	 * @param q
	 * @param dP
	 * @param dQ
	 * @param qInv
	 */
	public RSAKeyContents(char [] password, BigInteger modulus, BigInteger publicExponent,
			BigInteger privateExponent, BigInteger p, BigInteger q,
			BigInteger dP, BigInteger dQ, BigInteger qInv) {
		
		super(RSAKeyMetadata.createSecureDefault(password), modulus, publicExponent);
		
		this.privateExponent = privateExponent;
		this.p = p;
		this.q = q;
		this.dP = dP;
		this.dQ = dQ;
		this.qInv = qInv;
	}
    
	public RSAKeyContents(RSAKeyMetadata management, 
			BigInteger modulus, BigInteger publicExponent,
			BigInteger privateExponent, BigInteger p, BigInteger q,
			BigInteger dP, BigInteger dQ, BigInteger qInv) {
		
		super(management, modulus, publicExponent);
		
		this.privateExponent = privateExponent;
		this.p = p;
		this.q = q;
		this.dP = dP;
		this.dQ = dQ;
		this.qInv = qInv;
	}
	
	
	public final RSAKeyParameters getPrivateKey() {
		return new RSAPrivateCrtKeyParameters(modulus, publicExponent, privateExponent, p, q, dP, dQ, qInv);
	}

	
	
}
