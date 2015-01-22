package com.cryptoregistry.btls.handshake.validator;

import java.util.EventObject;

public class ValidationEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	boolean success;

	public ValidationEvent(Object arg0, boolean success) {
		super(arg0);
		this.success = success;
	}
	
	public ValidationEvent(Object arg0) {
		super(arg0);
		this.success = true;
	}

	public boolean isSuccess() {
		return success;
	}

}
