/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.client.storage;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.Buttermilk;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.client.security.SuitableMatchFailedException;

public class DataStoreTest {

	@Test
	public void test0() {
		String regHandle = "Chinese Knees";
		String path = System.getenv().get("BUTTERMILK_HOME");
		if(path == null) Assert.fail("Need a BUTTERMILK_HOME env variable");
		SimpleKeyManager keyManager = new SimpleKeyManager();
		System.err.println(keyManager);
		System.err.println(keyManager.keysExist());
		BDBDatastore ds = new BDBDatastore(keyManager);
		if(ds != null) Assert.assertTrue(true);
		
		try {
		
			ds.cleanOut();
			
			for(int i = 0; i<3; i++){
				CryptoKey key = Buttermilk.INSTANCE.generateC2Keys();
				ds.getViews().put(regHandle, key);
				key = Buttermilk.INSTANCE.generateECKeys("P-256");
				ds.getViews().put(regHandle, key);
				key = Buttermilk.INSTANCE.generateRSAKeys();
				ds.getViews().put(regHandle, key);
				key = Buttermilk.INSTANCE.generateRSAKeys();
				ds.getViews().put("stoney", key);
			//	key = Buttermilk.INSTANCE.generateNTRUKeys();
			//	ds.getViews().put(regHandle, key);
			//	key = Buttermilk.INSTANCE.generateSymmetricKey();
			//	ds.getViews().put(regHandle, key);
			}
			
			Assert.assertEquals(ds.getViews().getSecureMap().size(),12);
			Assert.assertEquals(ds.getViews().getMetadataMap().size(),12);
			
			Collection<Metadata> col = ds.getViews().getAllForRegHandle(regHandle);
			Assert.assertEquals(col.size(), 9);
			
			Assert.assertTrue(ds.getViews().hasRegHandle(regHandle));
			Assert.assertTrue(ds.getViews().hasRegHandle("stoney"));
			
			SingleResultCriteria criteria = SingleResultCriteria.c2(regHandle);
			try {
				ds.getViews().get(criteria);
				Assert.assertNotNull(criteria);
				Assert.assertNotNull(criteria.result);
				Assert.assertEquals("Curve25519", criteria.result.getMetadata().getKeyGenerationAlgorithm());
			} catch (SuitableMatchFailedException e) {
				Assert.fail();
			}
			
			
			criteria = SingleResultCriteria.ec(regHandle);
			try {
				ds.getViews().get(criteria);
				Assert.assertNotNull(criteria);
				Assert.assertNotNull(criteria.result);
				Assert.assertEquals("EC", criteria.result.getMetadata().getKeyGenerationAlgorithm());
				Assert.assertEquals("P-256", criteria.result.getMetadata().getCurveName());
			} catch (SuitableMatchFailedException e) {
				Assert.fail();
			}
			
			criteria = SingleResultCriteria.rsa(regHandle);
			try {
				ds.getViews().get(criteria);
				Assert.assertNotNull(criteria);
				Assert.assertNotNull(criteria.result);
				Assert.assertEquals("RSA", criteria.result.getMetadata().getKeyGenerationAlgorithm());
				Assert.assertEquals("2048", String.valueOf(criteria.result.getMetadata().getRSAKeySize()));
			} catch (SuitableMatchFailedException e) {
				Assert.fail();
			}
			
	
		}finally{
			if(ds != null){
				ds.close();
			}
		}
		
	}

}
