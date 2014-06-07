package com.cryptoregistry.rsa;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class RSAKeyMetadata implements CryptoKeyMetadata {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public RSAKeyMetadata(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static RSAKeyMetadata createDefault() {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static RSAKeyMetadata createForPublication() {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static RSAKeyMetadata createSecureDefault(char[]password) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static RSAKeyMetadata createSecureScrypt(char[]password) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}
	
	public static RSAKeyMetadata createSecure(PBEParams params) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base64url,params));
	}

	@Override
	public String getHandle() {
		return handle;
	}

	@Override
	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return KeyGenerationAlgorithm.RSA;
	}

	@Override
	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public KeyFormat getFormat() {
		return format;
	}

}
