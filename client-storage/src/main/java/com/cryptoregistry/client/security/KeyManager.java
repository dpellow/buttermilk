/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.security;


import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;

public interface KeyManager {

	/**
	 * Return the bytes for a symmetric key
	 * 
	 * @param password
	 * @return
	 */
	public SensitiveBytes loadKey(Password password);
	
	/**
	 * Return the obfuscated password for the security key (or keys) we are using
	 * 
	 * @return
	 */
	public Password getPassword();
	
	/**
	 * Return true at least one key exists in the designated location. 
	 * 
	 * @return
	 */
	public boolean keysExist();
	
	/**
	 * Return the path to where the data store implementation keeps its data (i.e. fully qualified root path)
	 * 
	 * @return
	 */
	public String getDatastoreFolder();
	
	//public Properties getProperties();

}