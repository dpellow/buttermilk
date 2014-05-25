package com.cryptoregistry.rsa;

import java.io.StringWriter;

import org.junit.Test;

import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.pbe.PBEParams;
import com.cyptoregistry.formats.Encoding;
import com.cyptoregistry.formats.JsonRSAFormatter;
import com.cyptoregistry.formats.Mode;

public class RSATest {

	@Test
	public void test0() {
		
		char[]p= {'p','a','s','s','w','o','r','d'};
		Password pass0 = new NewPassword(p);
		byte [] salt0 = {'s', 'a', 'l', 't'};
		SensitiveBytes sb = new SensitiveBytes(salt0);
		int iterations = 1000;
		
		PBEParams params = new PBEParams(PBEAlg.PBKDF2);
		params.setSalt(sb);
		params.setIterations(iterations);
		params.setPassword(pass0);
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		JsonRSAFormatter formatter = new JsonRSAFormatter(contents,params);
		StringWriter writer = new StringWriter();
		formatter.formatKeys(Mode.EXPOSED, Encoding.Base10, writer);
		System.err.println(writer.toString());
		writer = new StringWriter();
		formatter.formatKeys(Mode.EXPOSED, Encoding.Base64url, writer);
		System.err.println(writer.toString());
		writer = new StringWriter();
		formatter.formatKeys(Mode.SEALED, Encoding.Base64url, writer);
		System.err.println(writer.toString());
		writer = new StringWriter();
		formatter.formatKeys(Mode.PUBLISHED, Encoding.Base64url, writer);
		System.err.println(writer.toString());
	}

}
