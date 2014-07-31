package com.cryptoregistry.formats;

import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

/**
 * Encapsulate various modes for formatting
 * 
 * @author Dave
 *
 */
public class KeyFormat {

	public final EncodingHint encodingHint;
	public final Mode mode;
	public final PBEParams pbeParams;
	
	public KeyFormat() {
		encodingHint = EncodingHint.Base64url;
		mode = Mode.UNSECURED;
		pbeParams = null;
	}
	
	public KeyFormat(Mode mode) {
		encodingHint = EncodingHint.Base64url;
		this.mode = mode;
		pbeParams = null;
	}
	
	public KeyFormat(EncodingHint enc, Mode mode) {
		encodingHint = enc;
		this.mode = mode;
		pbeParams = null;
	}
	
	public KeyFormat(char [] password) {
		encodingHint = EncodingHint.Base64url;
		mode = Mode.SECURED;
		pbeParams = PBEParamsFactory.INSTANCE.createPBKDF2Params(password);
	}
	
	public KeyFormat(EncodingHint enc, char [] password) {
		encodingHint = enc;
		mode = Mode.SECURED;
		pbeParams = PBEParamsFactory.INSTANCE.createPBKDF2Params(password);
	}
	
	public KeyFormat(EncodingHint encoding, PBEParams params) {
		super();
		this.encodingHint = encoding;
		this.mode = Mode.SECURED;
		this.pbeParams = params;
	}

	public KeyFormat(EncodingHint encoding, Mode mode, PBEParams pbeParams) {
		super();
		this.encodingHint = encoding;
		this.mode = mode;
		this.pbeParams = pbeParams;
	}
	
	public KeyFormat clone() {
		KeyFormat f = new KeyFormat(this.encodingHint,this.mode,this.pbeParams.clone());
		return f;
	}

	@Override
	public String toString() {
		return "KeyFormat [encoding=" + encodingHint + ", mode=" + mode
				+ ", pbeParams=" + pbeParams + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((encodingHint == null) ? 0 : encodingHint.hashCode());
		result = prime * result + ((mode == null) ? 0 : mode.hashCode());
		result = prime * result
				+ ((pbeParams == null) ? 0 : pbeParams.hashCode());
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
		KeyFormat other = (KeyFormat) obj;
		if (encodingHint != other.encodingHint)
			return false;
		if (mode != other.mode)
			return false;
		if (pbeParams == null) {
			if (other.pbeParams != null)
				return false;
		} else if (!pbeParams.equals(other.pbeParams))
			return false;
		return true;
	}

}
