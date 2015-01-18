/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/**
 * used for finding the encrypted version of same 
 * 
 * @author Dave
 *
 */
public class Metadata implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean key;
	private boolean forPublication;
	private boolean contact;
	private boolean signature;
	private boolean namedList;
	private boolean namedMap;
	private String keyGenerationAlgorithm;
	private String signatureAlgorithm;
	private long createdOn;
	private String registrationHandle;
	private String curveName;
	private String NTRUParamName;
	private int RSAKeySize;
	
	private boolean ignore; // you can mark a record as out of scope - if true, it will not be returned by queries
	private boolean ephemeral; // intended to be an ephemeral key 
	
	public Metadata() {
		super();
	}
	
	/**
	 * Return true if and only if all the items in criteria match this meta data,
	 * fast fail if any non-match found in the search set
	 * 
	 * @param criteria
	 * @return
	 */
	public boolean match(Map<MetadataTokens,Object> criteria){
		
		if(ignore) return false;
		
		Iterator<MetadataTokens> iter = criteria.keySet().iterator();
		while(iter.hasNext()){
			MetadataTokens key = iter.next();
			Object value = criteria.get(key);
			switch(key){
				case key:{	 
					boolean val = (Boolean)value;
					if(this.key != val) return false;
					else continue;
				}
				case forPublication: {
					boolean val = (Boolean)value;
					if(this.forPublication != val) return false;
					else continue;
				}
				case contact: {
					boolean val = (Boolean)value;
					if(this.contact != val) return false;
					else continue;
				}
				case signature: {
					boolean val = (Boolean)value;
					if(this.signature != val) return false;
					else continue;
				}
				case namedList: {
					boolean val = (Boolean)value;
					if(this.namedList != val) return false;
					else continue;
				}
				case namedMap: {
					boolean val = (Boolean)value;
					if(this.namedMap != val) return false;
					else continue;
				}
				case keyGenerationAlgorithm: {
					String val = String.valueOf(value);
					if(!this.keyGenerationAlgorithm.equals(val)) return false;
					else continue;
				}
				case signatureAlgorithm:{
					String val = String.valueOf(value);
					if(!this.signatureAlgorithm.equals(val)) return false;
					else continue;
				}
				case createdOn: {
					String val = String.valueOf(value);
					if(!String.valueOf(createdOn).equals(val)) return false;
					else continue;
				}
				case registrationHandle: {
					String val = String.valueOf(value);
					if(!this.registrationHandle.equals(val)) return false;
					else continue;
				}
				case curveName: {
					String val = String.valueOf(value);
					if(curveName == null) return false;
					if(!this.curveName.equals(val)) return false;
					else continue;
				}
				case NTRUParamName: {
					String val = String.valueOf(value);
					if(NTRUParamName == null) return false;
					if(!this.NTRUParamName.equals(val)) return false;
					else continue;
				}
				case RSAKeySize: {
					Integer i = (Integer)value;
					if(this.RSAKeySize != i) return false;
					else continue;
				}
			}
			
		}
		return true;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public boolean isContact() {
		return contact;
	}

	public void setContact(boolean contact) {
		this.contact = contact;
	}

	public boolean isSignature() {
		return signature;
	}

	public void setSignature(boolean signature) {
		this.signature = signature;
	}

	public boolean isNamedList() {
		return namedList;
	}

	public void setNamedList(boolean namedList) {
		this.namedList = namedList;
	}

	public boolean isNamedMap() {
		return namedMap;
	}

	public void setNamedMap(boolean namedMap) {
		this.namedMap = namedMap;
	}

	public String getKeyGenerationAlgorithm() {
		return keyGenerationAlgorithm;
	}

	public void setKeyGenerationAlgorithm(String keyGenerationAlgorithm) {
		this.keyGenerationAlgorithm = keyGenerationAlgorithm;
	}

	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}

	public long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}

	public String getRegistrationHandle() {
		return registrationHandle;
	}

	public void setRegistrationHandle(String registrationHandle) {
		this.registrationHandle = registrationHandle;
	}

	public boolean isForPublication() {
		return forPublication;
	}

	public void setForPublication(boolean forPublication) {
		this.forPublication = forPublication;
	}

	public String getCurveName() {
		return curveName;
	}

	public void setCurveName(String curveName) {
		this.curveName = curveName;
	}

	public String getNTRUParamName() {
		return NTRUParamName;
	}

	public void setNTRUParamName(String nTRUParamName) {
		NTRUParamName = nTRUParamName;
	}

	public int getRSAKeySize() {
		return RSAKeySize;
	}

	public void setRSAKeySize(int rSAKeySize) {
		RSAKeySize = rSAKeySize;
	}

	public boolean isIgnore() {
		return ignore;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public boolean isEphemeral() {
		return ephemeral;
	}

	public void setEphemeral(boolean ephemeral) {
		this.ephemeral = ephemeral;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((NTRUParamName == null) ? 0 : NTRUParamName.hashCode());
		result = prime * result + RSAKeySize;
		result = prime * result + (contact ? 1231 : 1237);
		result = prime * result + (int) (createdOn ^ (createdOn >>> 32));
		result = prime * result
				+ ((curveName == null) ? 0 : curveName.hashCode());
		result = prime * result + (ephemeral ? 1231 : 1237);
		result = prime * result + (forPublication ? 1231 : 1237);
		result = prime * result + (ignore ? 1231 : 1237);
		result = prime * result + (key ? 1231 : 1237);
		result = prime
				* result
				+ ((keyGenerationAlgorithm == null) ? 0
						: keyGenerationAlgorithm.hashCode());
		result = prime * result + (namedList ? 1231 : 1237);
		result = prime * result + (namedMap ? 1231 : 1237);
		result = prime
				* result
				+ ((registrationHandle == null) ? 0 : registrationHandle
						.hashCode());
		result = prime * result + (signature ? 1231 : 1237);
		result = prime
				* result
				+ ((signatureAlgorithm == null) ? 0 : signatureAlgorithm
						.hashCode());
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
		Metadata other = (Metadata) obj;
		if (NTRUParamName == null) {
			if (other.NTRUParamName != null)
				return false;
		} else if (!NTRUParamName.equals(other.NTRUParamName))
			return false;
		if (RSAKeySize != other.RSAKeySize)
			return false;
		if (contact != other.contact)
			return false;
		if (createdOn != other.createdOn)
			return false;
		if (curveName == null) {
			if (other.curveName != null)
				return false;
		} else if (!curveName.equals(other.curveName))
			return false;
		if (ephemeral != other.ephemeral)
			return false;
		if (forPublication != other.forPublication)
			return false;
		if (ignore != other.ignore)
			return false;
		if (key != other.key)
			return false;
		if (keyGenerationAlgorithm == null) {
			if (other.keyGenerationAlgorithm != null)
				return false;
		} else if (!keyGenerationAlgorithm.equals(other.keyGenerationAlgorithm))
			return false;
		if (namedList != other.namedList)
			return false;
		if (namedMap != other.namedMap)
			return false;
		if (registrationHandle == null) {
			if (other.registrationHandle != null)
				return false;
		} else if (!registrationHandle.equals(other.registrationHandle))
			return false;
		if (signature != other.signature)
			return false;
		if (signatureAlgorithm == null) {
			if (other.signatureAlgorithm != null)
				return false;
		} else if (!signatureAlgorithm.equals(other.signatureAlgorithm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Metadata [key=" + key + "]";
	}
	

}
