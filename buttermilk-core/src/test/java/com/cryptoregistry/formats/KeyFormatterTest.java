package com.cryptoregistry.formats;

import java.io.StringWriter;

import org.junit.Test;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.pbe.PBEParamsFactory;

public class KeyFormatterTest {
	
	@Test 
	public void test1() {
		
		ECKeyContents c0 = com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys("P-256");
		
		// define a PBE
		char [] password = {'p','a','s','s'};
		PBEParams params0 = PBEParamsFactory.INSTANCE.createPBKDF2Params(password);
		params0.setIterations(params0.getIterations()+1);
		KeyFormat secureFormat = new KeyFormat(EncodingHint.Base64url,Mode.SECURED,params0);
		KeyFormat publicFormat = new KeyFormat(EncodingHint.Base64url,Mode.FOR_PUBLICATION,null);
		
		ECKeyContents c1 = c0.clone(secureFormat);
		ECKeyContents c2 = c0.clone(publicFormat);
		
		// define a different PBE
		params0 = PBEParamsFactory.INSTANCE.createScryptParams(password);
		secureFormat = new KeyFormat(EncodingHint.Base64url,Mode.SECURED,params0);
		ECKeyContents c3 = c0.clone(secureFormat);
		
		final String registrationHandle = "Chinese Eyes";
		JSONFormatter f = new JSONFormatter(registrationHandle);
		f.add(c2);
		f.add(c0);
		f.add(c1);
		f.add(c3);
		StringWriter writer = new StringWriter();
		f.format(writer);
		System.err.println(writer.toString());
	}

	@Test
	public void test0() {
		Curve25519KeyContents c0 = CryptoFactory.INSTANCE.generateKeys();
		
		System.err.println(c0.getFormat()); // by default, OPEN format and no PBE defined
		
		// define a PBE
		char [] password = {'p','a','s','s'};
		PBEParams params0 = PBEParamsFactory.INSTANCE.createPBKDF2Params(password);
		params0.setIterations(params0.getIterations()+1);
		KeyFormat secureFormat = new KeyFormat(EncodingHint.Base64url,Mode.SECURED,params0);
		KeyFormat publicFormat = new KeyFormat(EncodingHint.Base64url,Mode.FOR_PUBLICATION,null);
		
		Curve25519KeyContents c1 = c0.clone(secureFormat);
		Curve25519KeyContents c2 = c0.clone(publicFormat);
		
		// define a different PBE
		params0 = PBEParamsFactory.INSTANCE.createScryptParams(password);
		secureFormat = new KeyFormat(EncodingHint.Base64url,Mode.SECURED,params0);
		Curve25519KeyContents c3 = c0.clone(secureFormat);
		
		final String registrationHandle = "Chinese Eyes";
		JSONFormatter f = new JSONFormatter(registrationHandle);
		f.add(c2);
		f.add(c0);
		f.add(c1);
		f.add(c3);
		StringWriter writer = new StringWriter();
		f.format(writer);
		System.err.println(writer.toString());
		
		
	}

}
