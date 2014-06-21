package com.cryptoregistry.formats;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;

public class GeneralReadingTest {

	@Test
	public void test0() {
		File f = new File("src/test/resources/keys.test.json");
		JSONReader reader = new JSONReader(f);
		KeyMaterials km = reader.parse();
		List<CryptoKeyWrapper> list = km.keys();
		for(CryptoKeyWrapper wrapper: list){
			System.err.println(wrapper);
		}
		Assert.assertTrue(list.size() == 3);
	}

}
