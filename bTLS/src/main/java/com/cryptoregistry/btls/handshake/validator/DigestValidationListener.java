package com.cryptoregistry.btls.handshake.validator;


public interface DigestValidationListener {

	public void digestComparisonCompleted(ValidationEvent evt);
}
