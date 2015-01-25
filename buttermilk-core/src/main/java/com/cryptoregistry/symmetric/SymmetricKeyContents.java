package com.cryptoregistry.symmetric;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.c2.key.SecretKey;

/**
 * This is a container for (in essence) an array of bytes, possibly randomly generated, 
 * that we wish to protect and treat as a non-ephemeral key.
 * 
 * @author Dave
 *
 */
public class SymmetricKeyContents extends SecretKey implements CryptoKey  {

	public final SymmetricKeyMetadata metadata;
	
	public SymmetricKeyContents(SymmetricKeyMetadata metadata, byte[] bytes) {
		super(bytes);
		this.metadata = metadata;
	}

	@Override
	public CryptoKeyMetadata getMetadata() {
		return metadata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
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
		SymmetricKeyContents other = (SymmetricKeyContents) obj;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SymmetricKeyContents [metadata=" + metadata + "]";
	}

}
