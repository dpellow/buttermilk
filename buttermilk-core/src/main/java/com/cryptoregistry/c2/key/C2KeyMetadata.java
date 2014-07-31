package com.cryptoregistry.c2.key;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;


public class C2KeyMetadata implements CryptoKeyMetadata {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;

	public C2KeyMetadata(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	public C2KeyMetadata clone() {
		Date d = null;
		if(createdOn != null) d = new Date(createdOn.getTime());
		KeyFormat f = null;
		if(format != null) f = format.clone();
		return new C2KeyMetadata(this.handle,d,f);
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static C2KeyMetadata createUnsecured() {
		return new C2KeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static C2KeyMetadata createForPublication() {
		return new C2KeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static C2KeyMetadata createSecurePBKDF2(char[]password) {
		return new C2KeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static C2KeyMetadata createSecureScrypt(char[]password) {
		return new C2KeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}
	
	public static C2KeyMetadata createSecure(PBEParams params) {
		return new C2KeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,params));
	}

	@Override
	public String getHandle() {
		return handle;
	}
	
	public String getDistinguishedHandle() {
		return handle+"-"+format.mode.code;
	}

	@Override
	public KeyGenerationAlgorithm getKeyAlgorithm() {
		return  KeyGenerationAlgorithm.Curve25519;
	}

	@Override
	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public KeyFormat getFormat() {
		return format;
	}
	public C2KeyMetadata cloneForPublication() {
		return new C2KeyMetadata(handle, createdOn,new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public C2KeyMetadata cloneSecurePBKDF2(char[]password) {
		return new C2KeyMetadata(handle, createdOn, new KeyFormat(password));
	}
	
	public C2KeyMetadata cloneSecureScrypt(char[]password) {
		return new C2KeyMetadata(handle, createdOn,
				new KeyFormat(EncodingHint.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}

}
