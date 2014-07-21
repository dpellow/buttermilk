/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.pbe;

import java.io.IOException;
import java.util.Date;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

import net.iharder.Base64;

public abstract class ArmoredPBEResult {

	public String version = "Buttermilk PBE 1.0";
	public Date createdOn; 
	
	public String base64Enc;
	public String base64Salt;
	
	public ArmoredPBEResult(byte [] enc, byte [] salt) {
		super();
		try {
			this.base64Enc = Base64.encodeBytes(enc, Base64.URL_SAFE);
			this.base64Salt = Base64.encodeBytes(salt, Base64.URL_SAFE);
		}catch(Exception x){
			throw new RuntimeException(x);
		}
	}
	
	public ArmoredPBEResult(String base64Enc, String base64Salt) {
		super();
		this.base64Enc = base64Enc;
		this.base64Salt = base64Salt;
	}
	
	public ArmoredPBEResult(String version, Date createdOn, String base64Enc, String base64Salt) {
		super();
		this.version = version;
		this.createdOn = createdOn;
		this.base64Enc = base64Enc;
		this.base64Salt = base64Salt;
	}
	
	public abstract PBEParams generateParams(Password password) ;
	
	public byte [] getResultBytes(){
		try {
			return Base64.decode(base64Enc, Base64.URL_SAFE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public SensitiveBytes getResultWrapper() {
		return new SensitiveBytes(getResultBytes());
	}
	
	public byte [] getSaltBytes(){
		try {
			return Base64.decode(base64Salt, Base64.URL_SAFE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public SensitiveBytes getSaltWrapper() {
		return new SensitiveBytes(getSaltBytes());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((base64Enc == null) ? 0 : base64Enc.hashCode());
		result = prime * result
				+ ((base64Salt == null) ? 0 : base64Salt.hashCode());
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ArmoredPBEResult other = (ArmoredPBEResult) obj;
		if (base64Enc == null) {
			if (other.base64Enc != null)
				return false;
		} else if (!base64Enc.equals(other.base64Enc))
			return false;
		if (base64Salt == null) {
			if (other.base64Salt != null)
				return false;
		} else if (!base64Salt.equals(other.base64Salt))
			return false;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ArmoredPBEResult [version=" + version + ", createdOn="
				+ createdOn + ", base64Enc=" + base64Enc + ", base64Salt="
				+ base64Salt + "]";
	}
	
}
