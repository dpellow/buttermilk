/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

/**
 * Encapsulate query output - single-valued result
 * 
 * @author Dave
 *
 */
public class SingleResult {

	public Object result;
	public Metadata metadata;
	
	public SingleResult() {
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
