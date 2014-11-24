package com.cryptoregistry.client.security;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

public interface KeyManager {

	public abstract SensitiveBytes loadKey(Password password);
	public abstract Password getPassword();
	public boolean keysExist();

}