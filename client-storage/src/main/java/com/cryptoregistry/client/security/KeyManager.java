package com.cryptoregistry.client.security;

import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

public interface KeyManager {

	public SensitiveBytes loadKey(Password password);
	public Password getPassword();
	public boolean keysExist();
	public String getDatastoreFolder();

}