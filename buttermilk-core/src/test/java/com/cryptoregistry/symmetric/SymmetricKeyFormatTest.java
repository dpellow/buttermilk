package com.cryptoregistry.symmetric;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.formats.simplereader.JSONSymmetricKeyReader;
import com.cryptoregistry.passwords.NewPassword;

public class SymmetricKeyFormatTest {

	@Test
	public void test0() {
		char[] p = {'p','a','s','s'};
		SymmetricKeyContents s = CryptoFactory.INSTANCE.generateKey(new NewPassword(p),256);
		JSONFormatter format = new JSONFormatter("Chinese Knees");
		format.add(s);
		StringWriter writer = new StringWriter();
		format.format(writer);
		String out = writer.toString();
		JSONSymmetricKeyReader reader = new JSONSymmetricKeyReader(new StringReader(out),new NewPassword(p));
		SymmetricKeyContents contents = reader.parse();
		//System.out.println(contents);
		Assert.assertNotNull(contents);
		
	}

}
