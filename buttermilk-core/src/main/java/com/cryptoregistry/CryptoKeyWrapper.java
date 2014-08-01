/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry;

import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.passwords.Password;

public interface CryptoKeyWrapper {

	CryptoKeyMetadata getMetadata();
	Class<?> getWrappedType();
	boolean isForPublication();
	
	// capable of being unlocked, if we are for publication this also returns false
	boolean isSecure();
	
	// opens key contents, net result is contents becomes unsecure
	boolean unlock(Password password); // returns false if fails to unlock
	
	// net result is key contents becomes secure
	void lock(KeyFormat format); // throws RuntimeException if contents is ForPublication or there was error
	
	Object getKeyContents();
	void setKeyContents(Object obj);
	
}
