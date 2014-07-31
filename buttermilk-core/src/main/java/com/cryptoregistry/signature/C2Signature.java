/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature;

import com.cryptoregistry.util.ArmoredString;

/**
 * This is a signature using the EC variant of the KCDSA algorithm: http://en.wikipedia.org/wiki/KCDSA
 * 
 * @author Dave
 *
 */
public class C2Signature implements SignatureBytes {

	public final ArmoredString v;
	public final ArmoredString r; 
	
	public C2Signature(ArmoredString v, ArmoredString r) {
		super();
		this.v = v;
		this.r = r;
	}
	
	public C2Signature(byte[] v, byte[] h) {
		super();
		this.v = new ArmoredString(v);
		this.r = new ArmoredString(h);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
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
		C2Signature other = (C2Signature) obj;
		if (r == null) {
			if (other.r != null)
				return false;
		} else if (!r.equals(other.r))
			return false;
		if (v == null) {
			if (other.v != null)
				return false;
		} else if (!v.equals(other.v))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "C2Signature [v=" + v + ", h=" + r + "]";
	}

	@Override
	public byte[] b1() {
		return this.v.decodeToBytes();
	}

	@Override
	public byte[] b2() {
		return this.r.decodeToBytes();
	}

}
