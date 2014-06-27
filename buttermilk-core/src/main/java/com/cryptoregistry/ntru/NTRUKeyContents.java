package com.cryptoregistry.ntru;

import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionPrivateKeyParameters;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.Polynomial;

public class NTRUKeyContents extends NTRUKeyForPublication {
	
	public final Polynomial t;
	public final IntegerPolynomial fp;

	public NTRUKeyContents(NTRUEncryptionParameters params, 
			IntegerPolynomial h, Polynomial t, IntegerPolynomial fp) {
		super(params, h);
		this.t = t;
		this.fp = fp;
	}

	public NTRUKeyContents(NTRUKeyMetadata metadata, 
			NTRUEncryptionParameters params, IntegerPolynomial h,
			Polynomial t, IntegerPolynomial fp) {
		super(metadata, params, h);
		this.t = t;
		this.fp = fp;
	}

	public NTRUEncryptionPrivateKeyParameters getPrivateKey() {
		return new NTRUEncryptionPrivateKeyParameters(h,t,fp,params);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fp == null) ? 0 : fp.hashCode());
		result = prime * result + ((t == null) ? 0 : t.hashCode());
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
		NTRUKeyContents other = (NTRUKeyContents) obj;
		if (fp == null) {
			if (other.fp != null)
				return false;
		} else if (!fp.equals(other.fp))
			return false;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}

}
