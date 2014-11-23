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

}
