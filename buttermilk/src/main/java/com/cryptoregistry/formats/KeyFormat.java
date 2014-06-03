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

	public final Encoding encoding;
	public final Mode mode;
	public final PBEParams pbeParams;
	
	public KeyFormat() {
		encoding = Encoding.Base64url;
		mode = Mode.OPEN;
		pbeParams = null;
	}
	
	public KeyFormat(Mode mode) {
		encoding = Encoding.Base64url;
		this.mode = mode;
		pbeParams = null;
	}
	
	public KeyFormat(char [] password) {
		encoding = Encoding.Base64url;
		mode = Mode.SEALED;
		pbeParams = PBEParamsFactory.INSTANCE.createPBKDF2Params(password);
	}
	
	public KeyFormat(Encoding encoding, PBEParams params) {
		super();
		this.encoding = encoding;
		this.mode = Mode.SEALED;
		this.pbeParams = params;
	}

	public KeyFormat(Encoding encoding, Mode mode, PBEParams pbeParams) {
		super();
		this.encoding = encoding;
		this.mode = mode;
		this.pbeParams = pbeParams;
	}

}
