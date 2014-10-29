package com.cryptoregistry.btls;

import junit.framework.Assert;

import org.junit.Test;

public class BTLSSocketTest {

	@Test
	public void test0(){
		
		// create two keys
		//Curve25519KeyContents c0 = CryptoFactory.INSTANCE.generateKeys();
		//Curve25519KeyContents c1 = CryptoFactory.INSTANCE.generateKeys();
		
		Assert.assertNotNull(Configuration.CONFIG.clientKey());
		Assert.assertNotNull(Configuration.CONFIG.serverKey());
		
	
	}

}
