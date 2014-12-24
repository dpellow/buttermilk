/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2014 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.client.security.SimpleKeyManager;
import com.cryptoregistry.client.storage.file.FileBasedDataStore;

public class FileBasedDatastoreTest {

	@Test
	public void test0(){
		String path = System.getenv().get("BUTTERMILK_HOME");
		if(path == null) Assert.fail("Need a BUTTERMILK_HOME env variable");
		SimpleKeyManager keyManager = new SimpleKeyManager();
		FileBasedDataStore ds = new FileBasedDataStore(keyManager);
		CryptoKey key0 = ds.findSecuredKey("Chinese Knees", "de4ca270-c93a-40f6-9ef6-8ed92f43bb4d");
		Assert.assertNotNull(key0);
		CryptoKey key1 = ds.findUnsecuredKey("Chinese Knees", "de4ca270-c93a-40f6-9ef6-8ed92f43bb4d");
		Assert.assertNotNull(key1);
		CryptoKey key2 = ds.findPublishedKey("Chinese Knees", "de4ca270-c93a-40f6-9ef6-8ed92f43bb4d");
		Assert.assertNotNull(key2);
		
	}

}
