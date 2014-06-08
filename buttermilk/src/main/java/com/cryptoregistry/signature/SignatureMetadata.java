package com.cryptoregistry.signature;

import java.util.Date;
import java.util.UUID;

import com.cryptoregistry.SignatureAlgorithm;

public class SignatureMetadata {

	public final String handle; // unique identifier for this signature
	public final Date createdOn; // will use ISO 8601 format for String representation
	public final SignatureAlgorithm sigAlg; //known routines in Buttermilk
	public final String signedWith; // handle of key used to sign
	public final String signedBy; // registration handle of the signer key
	
	public SignatureMetadata(String handle, Date createdOn,
			SignatureAlgorithm sigAlg, String signedWith, String signedBy) {
		super();
		this.handle = handle;
		this.createdOn = createdOn;
		this.sigAlg = sigAlg;
		this.signedWith = signedWith;
		this.signedBy = signedBy;
	}

	public SignatureMetadata(SignatureAlgorithm sigAlg, String signedWith,String signedBy){
		this(UUID.randomUUID().toString(),new Date(),sigAlg,signedWith,signedBy);
	}

	@Override
	public String toString() {
		return "SignatureMetadata [getHandle()=" + getHandle() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result + ((sigAlg == null) ? 0 : sigAlg.hashCode());
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
		SignatureMetadata other = (SignatureMetadata) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		if (sigAlg != other.sigAlg)
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

	public SignatureAlgorithm getSigAlg() {
		return sigAlg;
	}

	public String getHandle() {
		return handle;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public String getSignedWith() {
		return signedWith;
	}

	public String getSignedBy() {
		return signedBy;
	}

}
