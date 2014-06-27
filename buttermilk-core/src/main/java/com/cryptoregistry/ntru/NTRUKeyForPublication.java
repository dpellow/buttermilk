package com.cryptoregistry.ntru;

import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionPublicKeyParameters;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;

public class NTRUKeyForPublication {

	public final NTRUKeyMetadata metadata;
	public final NTRUEncryptionParameters params; 
	public final IntegerPolynomial h;
	
	public NTRUKeyForPublication(NTRUEncryptionParameters params, IntegerPolynomial h) {
		metadata = NTRUKeyMetadata.createDefault();
		this.params = params;
		this.h = h;
	}
	
	public NTRUKeyForPublication(NTRUKeyMetadata metadata, NTRUEncryptionParameters params, IntegerPolynomial h) {
		this.metadata = metadata;
		this.params = params;
		this.h = h;
	}
	
	public NTRUEncryptionPublicKeyParameters getPublicKey() {
		return new NTRUEncryptionPublicKeyParameters(h,params);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((h == null) ? 0 : h.hashCode());
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
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
		NTRUKeyForPublication other = (NTRUKeyForPublication) obj;
		if (h == null) {
			if (other.h != null)
				return false;
		} else if (!h.equals(other.h))
			return false;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NTRUKeyForPublication [metadata=" + metadata + ", params="
				+ params + ", h=" + h + "]";
	}

}
