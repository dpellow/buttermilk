/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.rsa;

import java.math.BigInteger;
import java.util.Date;

import x.org.bouncycastle.crypto.params.RSAKeyParameters;

public class RSAKeyForPublication {
	
	public final String version;
	public final Date createdOn;
	
	public final String 	 handle;
	public final BigInteger  modulus;
	public final BigInteger  publicExponent;
	
	public RSAKeyForPublication(String version, Date createdOn, String handle,
			BigInteger modulus, BigInteger publicExponent) {
		super();
		this.version = version;
		this.createdOn = createdOn;
		this.handle = handle;
		this.modulus = modulus;
		this.publicExponent = publicExponent;
	}

	public final RSAKeyParameters getPublicKey() {
		return new RSAKeyParameters(false, modulus, publicExponent);
	}
	
	public final String toString() {
		return handle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result + ((modulus == null) ? 0 : modulus.hashCode());
		result = prime * result
				+ ((publicExponent == null) ? 0 : publicExponent.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		RSAKeyForPublication other = (RSAKeyForPublication) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
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
		if (publicExponent == null) {
			if (other.publicExponent != null)
				return false;
		} else if (!publicExponent.equals(other.publicExponent))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
