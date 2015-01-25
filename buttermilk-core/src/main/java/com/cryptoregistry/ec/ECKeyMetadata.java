/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.formats.EncodingHint;
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
	
	public ECKeyMetadata clone() {
		Date d = null;
		if(createdOn != null) d = new Date(createdOn.getTime());
		KeyFormat f = null;
		if(format != null) f = format.clone();
		return new ECKeyMetadata(this.handle,d,f);
	}
	
	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static ECKeyMetadata createDefault() {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static ECKeyMetadata createUnsecured() {
		return createDefault();
	}
	
	public static ECKeyMetadata createForPublication() {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static ECKeyMetadata createSecureDefault(char[]password) {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static ECKeyMetadata createSecureScrypt(char[]password) {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}
	
	public static ECKeyMetadata createSecure(PBEParams params) {
		return new ECKeyMetadata(UUID.randomUUID().toString(), new Date(),
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
		return KeyGenerationAlgorithm.EC;
	}

	@Override
	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public KeyFormat getFormat() {
		return format;
	}
	
	public ECKeyMetadata cloneForPublication() {
		return new ECKeyMetadata(handle, createdOn,new KeyFormat(Mode.FOR_PUBLICATION));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ECKeyMetadata other = (ECKeyMetadata) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (format == null) {
			if (other.format != null)
				return false;
		} else if (!format.equals(other.format))
			return false;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ECKeyMetadata [handle=" + handle + ", createdOn=" + createdOn
				+ ", format=" + format + "]";
	}
	
	

}
