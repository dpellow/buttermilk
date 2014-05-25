package com.cryptoregistry.rsa;

import java.math.BigInteger;
import java.util.UUID;

import x.org.bouncycastle.crypto.params.RSAKeyParameters;
import x.org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

/**
 * Output from Key Generation for RSA
 * 
 * @author Dave
 *
 */
public class RSAKeyContents {

	public final String 	 handle;
	public final BigInteger  modulus;
	public final BigInteger  publicExponent;
	public final BigInteger  privateExponent;
	public final BigInteger  p;
	public final BigInteger  q;
	public final BigInteger  dP;
	public final BigInteger  dQ;
	public final BigInteger  qInv;
    
	public RSAKeyContents(BigInteger modulus, BigInteger publicExponent,
			BigInteger privateExponent, BigInteger p, BigInteger q,
			BigInteger dP, BigInteger dQ, BigInteger qInv) {
		super();
		handle = UUID.randomUUID().toString();
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		this.privateExponent = privateExponent;
		this.p = p;
		this.q = q;
		this.dP = dP;
		this.dQ = dQ;
		this.qInv = qInv;
	}
	
	public RSAKeyParameters getPublicKey() {
		return new RSAKeyParameters(false, modulus, publicExponent);
	}
	
	public RSAKeyParameters getPrivateKey() {
		if(privateExponent == null) throw new RuntimeException("No private key data present");
		return new RSAPrivateCrtKeyParameters(modulus, publicExponent, privateExponent, p, q, dP, dQ, qInv);
	}
	
}
