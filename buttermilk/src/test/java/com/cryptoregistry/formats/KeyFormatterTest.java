package com.cryptoregistry.formats;

import java.io.StringWriter;

import org.junit.Test;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class KeyFormatterTest {

	@Test
	public void test0() {
		Curve25519KeyContents c0 = CryptoFactory.INSTANCE.generateKeys();
		
		System.err.println(c0.getFormat()); // by default, OPEN format and no PBE defined
		
		// define a PBE
		char [] password = {'p','a','s','s'};
		PBEParams params0 = PBEParamsFactory.INSTANCE.createPBKDF2Params(password);
		params0.setIterations(params0.getIterations()+1);
		KeyFormat secureFormat = new KeyFormat(Encoding.Base64url,Mode.SECURED,params0);
		KeyFormat publicFormat = new KeyFormat(Encoding.Base64url,Mode.FOR_PUBLICATION,null);
		
		Curve25519KeyContents c1 = c0.clone(secureFormat);
		Curve25519KeyContents c2 = c0.clone(publicFormat);
		
		final String registrationHandle = "Chinese Eyes";
		JSONBuilder f = new JSONBuilder(registrationHandle);
		f.add(c2);
		f.add(c0);
		f.add(c1);
		StringWriter writer = new StringWriter();
		f.format(writer);
		System.err.println(writer.toString());
	}

}
