package com.cryptoregistry.client.storage;

public class Metadata {

	private String type;
	private boolean key;
	private boolean contact;
	private boolean signature;
	private boolean namedList;
	private boolean namedMap;
	private String keyGenerationAlgorithm;
	private String signatureAlgorithm;
	private long createdOn;
	private boolean publicOnly;
	private String registrationHandle;

	
	public Metadata(String type, boolean key, boolean contact,
			boolean signature, boolean namedList, boolean namedMap,
			String keyGenerationAlgorithm, String signatureAlgorithm,
			long createdOn, boolean publicOnly, String registrationHandle) {
		super();
		this.type = type;
		this.key = key;
		this.contact = contact;
		this.signature = signature;
		this.namedList = namedList;
		this.namedMap = namedMap;
		this.keyGenerationAlgorithm = keyGenerationAlgorithm;
		this.signatureAlgorithm = signatureAlgorithm;
		this.createdOn = createdOn;
		this.publicOnly = publicOnly;
		this.registrationHandle = registrationHandle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public boolean isPublicOnly() {
		return publicOnly;
	}

	public void setPublicOnly(boolean publicOnly) {
		this.publicOnly = publicOnly;
	}

	public String getRegistrationHandle() {
		return registrationHandle;
	}

	public void setRegistrationHandle(String registrationHandle) {
		this.registrationHandle = registrationHandle;
	}

}
