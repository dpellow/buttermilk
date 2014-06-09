package com.cryptoregistry.signature;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

public class ECDSASignature implements SignatureData {
	
	public final BigInteger r;
	public final BigInteger s;
	
	public ECDSASignature(BigInteger r, BigInteger s) {
		super();
		this.r = r;
		this.s = s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((r == null) ? 0 : r.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
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
		ECDSASignature other = (ECDSASignature) obj;
		if (r == null) {
			if (other.r != null)
				return false;
		} else if (!r.equals(other.r))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

	@Override
	public void formatJSON(JsonGenerator g, Writer writer)
			throws JsonGenerationException, IOException {
		
	}

}
