/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.signature;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.cryptoregistry.util.ArmoredString;

/**
 * <pre>
 * Holder for a signature's data
 * </pre>
 * @author Dave
 *
 */
public class CryptoSignature implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String handle; // unique identifier for this signature
	private Date createdOn; // will use ISO 8601 format for String rep
	private String signedWith; // handle of key used to sign
	private String signedBy; // registration handle the signer key
	
	private ArmoredString signature;
	private ArmoredString hash;
	private String sigAlgorithm;
	private String hashAlgorithm;

	private Map<Integer,String> dataRefs;
	
	public CryptoSignature() {
		super();
		dataRefs = new LinkedHashMap<Integer,String>();
	}

	public CryptoSignature(String handle, 
			Date createdOn, 
			String signedWith,
			String signedBy, 
			ArmoredString signature, 
			ArmoredString hash,
			String sigAlgorithm, 
			String hashAlgorithm) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.signedWith = signedWith;
		this.signedBy = signedBy;
		this.signature = signature;
		this.hash = hash;
		this.sigAlgorithm = sigAlgorithm;
		this.hashAlgorithm = hashAlgorithm;
	}

	public void addDataReference(String ref){
		Set<Integer> set = dataRefs.keySet();
		Integer key = new Integer(set.size()+1);
		dataRefs.put(key, ref);
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getSignedWith() {
		return signedWith;
	}

	public void setSignedWith(String signedWith) {
		this.signedWith = signedWith;
	}

	public String getSignedBy() {
		return signedBy;
	}

	public void setSignedBy(String signedBy) {
		this.signedBy = signedBy;
	}

	public ArmoredString getSignature() {
		return signature;
	}

	public void setSignature(ArmoredString signature) {
		this.signature = signature;
	}

	public ArmoredString getHash() {
		return hash;
	}

	public void setHash(ArmoredString hash) {
		this.hash = hash;
	}

	public String getSigAlgorithm() {
		return sigAlgorithm;
	}

	public void setSigAlgorithm(String sigAlgorithm) {
		this.sigAlgorithm = sigAlgorithm;
	}

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public Map<Integer, String> getDataRefs() {
		return dataRefs;
	}

	public void setDataRefs(Map<Integer, String> dataRefs) {
		this.dataRefs = dataRefs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result
				+ ((dataRefs == null) ? 0 : dataRefs.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result
				+ ((hashAlgorithm == null) ? 0 : hashAlgorithm.hashCode());
		result = prime * result
				+ ((sigAlgorithm == null) ? 0 : sigAlgorithm.hashCode());
		result = prime * result
				+ ((signature == null) ? 0 : signature.hashCode());
		result = prime * result
				+ ((signedBy == null) ? 0 : signedBy.hashCode());
		result = prime * result
				+ ((signedWith == null) ? 0 : signedWith.hashCode());
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
		CryptoSignature other = (CryptoSignature) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (dataRefs == null) {
			if (other.dataRefs != null)
				return false;
		} else if (!dataRefs.equals(other.dataRefs))
			return false;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (hashAlgorithm == null) {
			if (other.hashAlgorithm != null)
				return false;
		} else if (!hashAlgorithm.equals(other.hashAlgorithm))
			return false;
		if (sigAlgorithm == null) {
			if (other.sigAlgorithm != null)
				return false;
		} else if (!sigAlgorithm.equals(other.sigAlgorithm))
			return false;
		if (signature == null) {
			if (other.signature != null)
				return false;
		} else if (!signature.equals(other.signature))
			return false;
		if (signedBy == null) {
			if (other.signedBy != null)
				return false;
		} else if (!signedBy.equals(other.signedBy))
			return false;
		if (signedWith == null) {
			if (other.signedWith != null)
				return false;
		} else if (!signedWith.equals(other.signedWith))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CryptoSignature [handle=" + handle + "]";
	}
	
}

