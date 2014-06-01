package com.cryptoregistry.c2.key;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.passwords.NewPassword;


public class C2KeyManagement implements CryptoKey {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public C2KeyManagement(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static C2KeyManagement createDefault() {
		return new C2KeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static C2KeyManagement createForPublication() {
		return new C2KeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static C2KeyManagement createSecureDefault(char[]password) {
		return new C2KeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static C2KeyManagement createSecureHexDefault(char[]password) {
		return new C2KeyManagement(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base16,Mode.SEALED,new NewPassword(password)));
	}

	@Override
	public String getHandle() {
		return handle;
	}

	@Override
	public String getKeyAlgorithm() {
		return "Curve25519";
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
