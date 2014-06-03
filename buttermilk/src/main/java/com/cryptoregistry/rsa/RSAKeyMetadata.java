package com.cryptoregistry.rsa;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.passwords.NewPassword;

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
	
	public static RSAKeyMetadata createSecureHexDefault(char[]password) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base16,Mode.SEALED,new NewPassword(password)));
	}

	@Override
	public String getHandle() {
		return handle;
	}

	@Override
	public String getKeyAlgorithm() {
		return "RSA";
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
