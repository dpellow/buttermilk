package com.cryptoregistry.symmetric;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;


public class SymmetricKeyMetadata implements CryptoKeyMetadata {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public SymmetricKeyMetadata(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	public static SymmetricKeyMetadata createSecureDefault(char[]password) {
		return new SymmetricKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static SymmetricKeyMetadata createUnsecure() {
		return new SymmetricKeyMetadata(UUID.randomUUID().toString(), new Date(), new KeyFormat());
	}
	
	public static SymmetricKeyMetadata createSecureScrypt(char[]password) {
		return new SymmetricKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}
	
	public static SymmetricKeyMetadata createSecure(PBEParams params) {
		return new SymmetricKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,params));
	}

	@Override
	public String getHandle() {
		return handle;
	}

	@Override
	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return KeyGenerationAlgorithm.Symmetric;
	}

	@Override
	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public KeyFormat getFormat() {
		return format;
	}

	@Override
	public String getDistinguishedHandle() {
		return handle+"-"+format.mode.code;
	}

}
