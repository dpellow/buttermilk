/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.io.Serializable;
import java.util.Arrays;

/**
 * The secure tuple has a byte array for the encrypted data and its own IV per record. 
 *  
 * @author Dave
 *
 */
public class SecureData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private byte [] data;
	private byte [] iv;
	
	private String protoClass;

	public SecureData(byte[] data, byte[] iv, String protoClass) {
		this();
		this.data = data;
		this.iv = iv;
		this.protoClass = protoClass;
	}

	public SecureData() {
		super();
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getIv() {
		return iv;
	}

	public void setIv(byte[] iv) {
		this.iv = iv;
	}

	public String getProtoClass() {
		return protoClass;
	}

	public void setProtoClass(String protoClass) {
		this.protoClass = protoClass;
	}

	@Override
	public String toString() {
		return "SecureData [data=" + Arrays.toString(data) + ", iv="
				+ Arrays.toString(iv) + ", protoClass=" + protoClass + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + Arrays.hashCode(iv);
		result = prime * result
				+ ((protoClass == null) ? 0 : protoClass.hashCode());
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
		SecureData other = (SecureData) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		if (!Arrays.equals(iv, other.iv))
			return false;
		if (protoClass == null) {
			if (other.protoClass != null)
				return false;
		} else if (!protoClass.equals(other.protoClass))
			return false;
		return true;
	}

}
