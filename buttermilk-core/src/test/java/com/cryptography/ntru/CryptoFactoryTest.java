package com.cryptography.ntru;

import java.io.StringWriter;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.formats.JSONBuilder;
import com.cryptoregistry.ntru.CryptoFactory;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUNamedParameters;

public class CryptoFactoryTest {

	@Test
	public void test0() {
		
		byte [] in = "Hello NTRU world".getBytes(Charset.forName("UTF-8"));
		NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys();
		
		byte [] encrypted = CryptoFactory.INSTANCE.encrypt(sKey, in);
		byte [] out = CryptoFactory.INSTANCE.decrypt(sKey, encrypted);
		Assert.assertTrue(Arrays.areEqual(in, out));
	}
	
	@Test
	public void test1() {
		
		byte [] in = "Hello NTRU world".getBytes(Charset.forName("UTF-8"));
		for(NTRUNamedParameters p: NTRUNamedParameters.values()){
			System.out.println(p.toString());
			NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys(p.getKeyGenerationParameters());
			byte [] encrypted = CryptoFactory.INSTANCE.encrypt(sKey, in);
			byte [] out = CryptoFactory.INSTANCE.decrypt(sKey, encrypted);
			Assert.assertTrue(Arrays.areEqual(in, out));
		}
	}
	
	@Test
	public void test2() {
		
		for(NTRUNamedParameters p: NTRUNamedParameters.values()){
			System.out.println(p.toString());
			NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys(p.getKeyGenerationParameters());
			
			final String registrationHandle = "Chinese Eyes";
			JSONBuilder f = new JSONBuilder(registrationHandle);
			f.add(sKey);
			StringWriter writer = new StringWriter();
			f.format(writer);
			System.err.println(writer.toString());
		}
	}

}
