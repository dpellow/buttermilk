package com.cryptoregistry.client.storage;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.client.security.SimpleKeyManager;

public class DataStoreTest {

	@Test
	public void test0() {
		String path = System.getenv().get("BUTTERMILK_HOME");
		if(path == null) Assert.fail("Need a BUTTERMILK_HOME env variable");
		SimpleKeyManager keyManager = new SimpleKeyManager();
		System.err.println(keyManager);
		System.err.println(keyManager.keysExist());
		DataStore ds = new DataStore(keyManager);
		if(ds != null) Assert.assertTrue(true);
		if(ds != null){
			ds.closeDb();
		}
		
	}

}
