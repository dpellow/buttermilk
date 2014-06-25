/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.math.BigInteger;
import java.util.Date;

import com.cryptoregistry.Signer;
import com.cryptoregistry.formats.KeyFormat;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public class ECKeyContents extends ECKeyForPublication implements Signer {
	
	public final BigInteger d;
	
	/**
	 * Formatting is Mode.Open
	 * @param q
	 * @param curveName
	 * @param d
	 */
	public ECKeyContents(ECPoint q, String curveName, BigInteger d) {
		super(ECKeyMetadata.createDefault(), q, curveName);
		this.d = d;
	}
	
	public ECKeyContents(char [] password, ECPoint q, String curveName, BigInteger d) {
		super(ECKeyMetadata.createSecureDefault(password), q, curveName);
		this.d = d;
	}
	
	public ECKeyContents(ECKeyMetadata metadata, ECPoint q, String curveName, BigInteger d) {
		super(metadata, q, curveName);
		this.d = d;
	}
	
	public ECPrivateKeyParameters getPrivateKey() {
		ECDomainParameters domain = CurveFactory.getCurveForName(curveName);
		 ECPrivateKeyParameters params = new ECPrivateKeyParameters(d, domain);
		 return params;
	}
	
	public ECKeyContents clone(){
		ECKeyMetadata meta = metadata.clone();
		return new ECKeyContents(meta,Q,curveName,d);
	}
	
	public ECKeyContents clone(KeyFormat format){
		ECKeyMetadata meta = new ECKeyMetadata(this.getHandle(),new Date(this.getCreatedOn().getTime()),format);
		return new ECKeyContents(meta,Q,curveName,d);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((d == null) ? 0 : d.hashCode());
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
		ECKeyContents other = (ECKeyContents) obj;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}

}
