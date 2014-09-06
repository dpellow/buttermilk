package com.cryptoregistry.client.storage;

import java.io.Serializable;

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
	
	public Metadata() {
		super();
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

	@Override
	public String toString() {
		return "Metadata [key=" + key + ", forPublication=" + forPublication
				+ ", contact=" + contact + ", signature=" + signature
				+ ", namedList=" + namedList + ", namedMap=" + namedMap
				+ ", keyGenerationAlgorithm=" + keyGenerationAlgorithm
				+ ", signatureAlgorithm=" + signatureAlgorithm + ", createdOn="
				+ createdOn + ", registrationHandle=" + registrationHandle
				+ "]";
	}

}
