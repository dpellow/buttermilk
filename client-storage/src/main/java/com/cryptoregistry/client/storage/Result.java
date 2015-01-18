package com.cryptoregistry.client.storage;


public class Result {

	public Object result;
	public Metadata metadata;
	
	public Result() {
		super();
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
