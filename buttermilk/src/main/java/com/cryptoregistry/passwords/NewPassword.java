/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013-2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.passwords;

import java.util.Arrays;

/**
 * Wrapper for a char array - a password value - we intend to remove 
 * from heap memory as soon as is feasible, i.e., manually. This is done
 * to protect from leaks in heap dumps, etc. It is not possible to completely
 * remove such leaks - but we can try. 
 * 
 * @author Dave
 *
 */
public class NewPassword extends Password {

	public NewPassword(char[] password) {
		super(password);
	}
	
	public ExistingPassword promote() {
		char [] chars = this.getPassword();
		return new ExistingPassword(chars);
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
