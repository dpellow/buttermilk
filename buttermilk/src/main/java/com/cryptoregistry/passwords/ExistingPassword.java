/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.passwords;

import java.util.Arrays;

public class ExistingPassword extends Password {

	public ExistingPassword(char[] password) {
		super(password);
	}
	
	/**
	 * Copy the chars to make a new and disposable NewPassword instance from this existing one
	 * @return
	 */
	public NewPassword createNewPassword() {
		char [] newPass = new char[this.getPassword().length];
		System.arraycopy(this.getPassword(), 0, newPass, 0, newPass.length);
		return new NewPassword(newPass);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (alive ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(password);
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
		Password other = (Password) obj;
		if (alive != other.alive)
			return false;
		if (!Arrays.equals(password, other.password))
			return false;
		return true;
	}
}
