/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.Serializable;

public class SecureKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String handle;
	private String protoName; // the proto name, e.g. RSAKeyContentsProto

	public SecureKey() {
		super();
	}

	public SecureKey(String handle, String protoName) {
		super();
		this.handle = handle;
		this.protoName = protoName;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getProtoName() {
		return protoName;
	}

	public void setProtoName(String protoName) {
		this.protoName = protoName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		result = prime * result
				+ ((protoName == null) ? 0 : protoName.hashCode());
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
		SecureKey other = (SecureKey) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		if (protoName == null) {
			if (other.protoName != null)
				return false;
		} else if (!protoName.equals(other.protoName))
			return false;
		return true;
	}

	
}
