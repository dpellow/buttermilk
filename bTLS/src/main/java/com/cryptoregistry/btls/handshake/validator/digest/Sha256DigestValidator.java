package com.cryptoregistry.btls.handshake.validator.digest;

import com.cryptoregistry.btls.handshake.Handshake;

public class Sha256DigestValidator extends BaseDigestValidator {

	Handshake handshake;
	
	public Sha256DigestValidator(Handshake handshake) {
		this.handshake = handshake;
	}

	@Override
	public void validate() {
		

	}

}
