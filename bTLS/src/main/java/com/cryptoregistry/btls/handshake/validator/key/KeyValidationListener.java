package com.cryptoregistry.btls.handshake.validator.key;

import com.cryptoregistry.btls.handshake.validator.ValidationEvent;

public interface KeyValidationListener {

	public void keyValidationResult(ValidationEvent evt);
}
