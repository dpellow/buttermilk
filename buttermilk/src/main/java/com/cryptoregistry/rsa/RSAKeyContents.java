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
	
	public RSAKeyContents(String handle, BigInteger modulus, BigInteger publicExponent,
			BigInteger privateExponent, BigInteger p, BigInteger q,
			BigInteger dP, BigInteger dQ, BigInteger qInv) {
		
		this.handle = handle;
		this.modulus = modulus;
		this.publicExponent = publicExponent;
		this.privateExponent = privateExponent;
		this.p = p;
		this.q = q;
		this.dP = dP;
		this.dQ = dQ;
		this.qInv = qInv;
	}
    
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dP == null) ? 0 : dP.hashCode());
		result = prime * result + ((dQ == null) ? 0 : dQ.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result + ((modulus == null) ? 0 : modulus.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		result = prime * result
				+ ((privateExponent == null) ? 0 : privateExponent.hashCode());
		result = prime * result
				+ ((publicExponent == null) ? 0 : publicExponent.hashCode());
		result = prime * result + ((q == null) ? 0 : q.hashCode());
		result = prime * result + ((qInv == null) ? 0 : qInv.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
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
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		if (modulus == null) {
			if (other.modulus != null)
				return false;
		} else if (!modulus.equals(other.modulus))
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
		if (publicExponent == null) {
			if (other.publicExponent != null)
				return false;
		} else if (!publicExponent.equals(other.publicExponent))
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
	
	public final String toString() {
		return handle;
	}
	
}
