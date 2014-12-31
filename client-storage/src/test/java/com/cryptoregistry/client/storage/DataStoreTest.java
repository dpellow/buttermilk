package com.cryptoregistry.client.storage;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.Buttermilk;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.client.security.SimpleKeyManager;
import com.cryptoregistry.client.security.SuitableMatchFailedException;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.rsa.RSAKeyContents;

public class DataStoreTest {

	@Test
	public void test0() {
		String regHandle = "Chinese Knees";
		String path = System.getenv().get("BUTTERMILK_HOME");
		if(path == null) Assert.fail("Need a BUTTERMILK_HOME env variable");
		SimpleKeyManager keyManager = new SimpleKeyManager();
		System.err.println(keyManager);
		System.err.println(keyManager.keysExist());
		DataStore ds = new DataStore(keyManager);
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
			//	key = Buttermilk.INSTANCE.generateNTRUKeys();
			//	ds.getViews().put(regHandle, key);
			//	key = Buttermilk.INSTANCE.generateSymmetricKey();
			//	ds.getViews().put(regHandle, key);
			}
			
			Assert.assertEquals(ds.getViews().getSecureMap().size(),9);
			Assert.assertEquals(ds.getViews().getMetadataMap().size(),9);
			
			Map<MetadataTokens,Object> criteria = new HashMap<MetadataTokens,Object>();
			criteria.put(MetadataTokens.key, true);
			criteria.put(MetadataTokens.forPublication, false);
			criteria.put(MetadataTokens.keyGenerationAlgorithm, "Curve25519");
			try {
				Curve25519KeyContents contents = (Curve25519KeyContents) ds.getViews().getSecure(criteria);
				Assert.assertNotNull(contents);
			} catch (SuitableMatchFailedException e) {
				Assert.fail();
			}
			
			
			criteria.put(MetadataTokens.keyGenerationAlgorithm, "EC");
			criteria.put(MetadataTokens.curveName, "P-256");
			try {
				ECKeyContents contents = (ECKeyContents) ds.getViews().getSecure(criteria);
				Assert.assertNotNull(contents);
			} catch (SuitableMatchFailedException e) {
				Assert.fail();
			}
			
			criteria.put(MetadataTokens.keyGenerationAlgorithm, "RSA");
			criteria.put(MetadataTokens.RSAKeySize, 2048);
			criteria.remove(MetadataTokens.curveName);
			try {
				RSAKeyContents contents = (RSAKeyContents) ds.getViews().getSecure(criteria);
				Assert.assertNotNull(contents);
			} catch (SuitableMatchFailedException e) {
				Assert.fail();
			}
			
			try {
				Curve25519KeyContents contents = ds.getViews().getMostRecentC2Key(regHandle);
				Assert.assertNotNull(contents);
			} catch (SuitableMatchFailedException e) {
				e.printStackTrace();
			}
			
			try {
				ECKeyContents contents = ds.getViews().getMostRecentECKey(regHandle, "P-256");
				Assert.assertNotNull(contents);
			} catch (SuitableMatchFailedException e) {
				e.printStackTrace();
			}
			
			
	
		}finally{
			if(ds != null){
				ds.closeDb();
			}
		}
		
	}

}
