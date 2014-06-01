package com.cryptoregistry.rsa;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.passwords.NewPassword;

public class RSAKeyManagement implements CryptoKey {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public RSAKeyManagement(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static RSAKeyManagement createDefault() {
		return new RSAKeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static RSAKeyManagement createForPublication() {
		return new RSAKeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static RSAKeyManagement createSecureDefault(char[]password) {
		return new RSAKeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static RSAKeyManagement createSecureHexDefault(char[]password) {
		return new RSAKeyManagement(UUID.randomUUID().toString(), new Date(),
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
