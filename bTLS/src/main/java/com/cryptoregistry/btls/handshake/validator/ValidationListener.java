package com.cryptoregistry.btls.handshake.validator;

public interface ValidationListener {

	public void validationComplete(ValidationEvent evt);
}
