/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.rsa;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class RSAKeyMetadata implements CryptoKeyMetadata {

	public final String handle;
	public final Date createdOn;
	public final KeyFormat format;
	// these are set on creation of the key from the key generation params
	public int strength; // requested strength on creation - e.g., 2048 bits
	public int certainty; // the number of iterations of the Miller-Rabin primality test we passed in
	
	public RSAKeyMetadata(String handle, Date createdOn, KeyFormat format) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
	}
	
	public RSAKeyMetadata(String handle, Date createdOn, KeyFormat format, int strength, int certainty) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.format = format;
		this.strength=strength;
		this.certainty=certainty;
	}

	/**
	 * Returns a default handle, createOn, and KeyFormat for base64Encode, Mode.OPEN
	 * @return
	 */
	public static RSAKeyMetadata createDefault() {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat());
	}
	
	public static RSAKeyMetadata createDefault(String handle) {
		return new RSAKeyMetadata(handle, new Date(),new KeyFormat());
	}
	
	public static RSAKeyMetadata createForPublication() {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(Mode.FOR_PUBLICATION));
	}
	
	public static RSAKeyMetadata createSecureDefault(char[]password) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),new KeyFormat(password));
	}
	
	public static RSAKeyMetadata createSecurePBKDF2(char[]password) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,
						PBEParamsFactory.INSTANCE.createPBKDF2Params(password)));
	}
	
	public static RSAKeyMetadata createSecureScrypt(char[]password) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),
				new KeyFormat(EncodingHint.Base64url,
						PBEParamsFactory.INSTANCE.createScryptParams(password)));
	}
	
	public static RSAKeyMetadata createSecure(PBEParams params) {
		return new RSAKeyMetadata(UUID.randomUUID().toString(), new Date(),
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + certainty;
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result + strength;
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
		RSAKeyMetadata other = (RSAKeyMetadata) obj;
		if (certainty != other.certainty)
			return false;
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
		if (strength != other.strength)
			return false;
		return true;
	}

	public RSAKeyMetadata cloneForPublication() {
		RSAKeyMetadata m = new RSAKeyMetadata(handle, createdOn,new KeyFormat(Mode.FOR_PUBLICATION));
		m.setCertainty(this.certainty);
		m.setStrength(this.strength);
		return m;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getCertainty() {
		return certainty;
	}

	public void setCertainty(int certainty) {
		this.certainty = certainty;
	}

}
