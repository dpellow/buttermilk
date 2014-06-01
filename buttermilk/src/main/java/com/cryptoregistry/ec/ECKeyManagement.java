package com.cryptoregistry.ec;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.passwords.NewPassword;

public class ECKeyManagement implements CryptoKey {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public ECKeyManagement(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static ECKeyManagement createDefault() {
		return new ECKeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static ECKeyManagement createForPublication() {
		return new ECKeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static ECKeyManagement createSecureDefault(char[]password) {
		return new ECKeyManagement(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static ECKeyManagement createSecureHexDefault(char[]password) {
		return new ECKeyManagement(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base16,Mode.SEALED,new NewPassword(password)));
	}

	@Override
	public String getHandle() {
		return handle;
	}

	@Override
	public String getKeyAlgorithm() {
		return "EC";
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
