package com.cryptoregistry.ec;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class ECKeyMetadata implements CryptoKeyMetadata {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public ECKeyMetadata(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static ECKeyMetadata createDefault() {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static ECKeyMetadata createForPublication() {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static ECKeyMetadata createSecureDefault(char[]password) {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static ECKeyMetadata createSecureScrypt(char[]password) {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}
	
	public static ECKeyMetadata createSecure(PBEParams params) {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(Encoding.Base64url,params));
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
