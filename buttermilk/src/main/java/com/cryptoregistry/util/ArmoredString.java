/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import net.iharder.Base64;

/** <pre>
 *
 * Wrapper for a Base64 URL-safe encoded String (base64url). The idea for this class comes from my reading of
 * http://salmon-protocol.googlecode.com/svn/trunk/draft-panzer-magicsig-01.html
 * 
 * See also http://tools.ietf.org/html/rfc4648#page-7
 *
 * </pre>
 * @author Dave
 *
 */
public class ArmoredString implements CharSequence,Serializable {

	private static final long serialVersionUID = 1L;
	
	public final String data;
	
	public ArmoredString(String encoded) {
		if(encoded == null) encoded = "";
		this.data = encoded;
	}

	public ArmoredString(byte[] bytes) {
		if(bytes == null)throw new RuntimeException("Cannot armor a null value");
		try {
			data=Base64.encodeBytes(bytes, Base64.URL_SAFE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Use when an unencoded String needs to be armored
	 * 
	 * @param unencoded
	 * @return
	 */
	public static ArmoredString fromString(String unencoded) {
		try {
			byte [] bytes = unencoded.getBytes("UTF-8");
			return new ArmoredString(bytes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public int length() {
		return data.length();
	}

	@Override
	public char charAt(int index) {
		return data.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return data.subSequence(start, end);
	}
	
	public String decode() {
		try {
			return new String(Base64.decode(data, Base64.URL_SAFE), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte [] decodeToBytes() {
		try {
			return Base64.decode(data, Base64.URL_SAFE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String plainVanillaBase64() {
		try {
			byte [] bytes = Base64.decode(data, Base64.URL_SAFE);
			return Base64.encodeBytes(bytes, Base64.NO_OPTIONS);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String toString() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		ArmoredString other = (ArmoredString) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}

}
