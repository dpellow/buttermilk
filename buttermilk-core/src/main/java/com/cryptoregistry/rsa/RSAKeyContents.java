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
    
	public RSAKeyContents(RSAKeyMetadata metadata, 
			BigInteger modulus, BigInteger publicExponent,
			BigInteger privateExponent, BigInteger p, BigInteger q,
			BigInteger dP, BigInteger dQ, BigInteger qInv) {
		
		super(metadata, modulus, publicExponent);
		
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dP == null) ? 0 : dP.hashCode());
		result = prime * result + ((dQ == null) ? 0 : dQ.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result
				+ ((privateExponent == null) ? 0 : privateExponent.hashCode());
		result = prime * result + ((q == null) ? 0 : q.hashCode());
		result = prime * result + ((qInv == null) ? 0 : qInv.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RSAKeyContents other = (RSAKeyContents) obj;
		if (dP == null) {
			if (other.dP != null)
				return false;
		} else if (!dP.equals(other.dP))
			return false;
		if (dQ == null) {
			if (other.dQ != null)
				return false;
		} else if (!dQ.equals(other.dQ))
			return false;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		if (privateExponent == null) {
			if (other.privateExponent != null)
				return false;
		} else if (!privateExponent.equals(other.privateExponent))
			return false;
		if (q == null) {
			if (other.q != null)
				return false;
		} else if (!q.equals(other.q))
			return false;
		if (qInv == null) {
			if (other.qInv != null)
				return false;
		} else if (!qInv.equals(other.qInv))
			return false;
		return true;
	}
	
}
