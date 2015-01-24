package com.cryptoregistry.btls.handshake.validator.digest;

import com.cryptoregistry.btls.handshake.validator.ValidationEvent;

public interface DigestValidationListener {

	public void digestComparisonCompleted(ValidationEvent evt);
}
