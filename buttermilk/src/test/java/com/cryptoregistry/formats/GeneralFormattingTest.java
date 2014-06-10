package com.cryptoregistry.formats;

import java.io.StringWriter;

import org.junit.Test;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.builder.RSAKeyContentsIterator;
import com.cryptoregistry.signature.builder.RSAKeySignatureBuilder;

public class GeneralFormattingTest {

	@Test
	public void test0() {
		
		CryptoContact contact = new CryptoContact();
		contact.add("GivenName.0", "David");
		contact.add("GivenName.1", "Richard");
		contact.add("FamilyName.0", "Smith");
		contact.add("Address.0", "1714 Roberts Ct.");
		contact.add("City", "Madison");
		contact.add("State", "Wisconsin");
		contact.add("PostalCode", "53711");
		contact.add("CountryCode", "US");
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		RSAKeyContentsIterator iter = new RSAKeyContentsIterator(contents);
		RSAKeySignatureBuilder sigBuilder = new RSAKeySignatureBuilder("Chinese Eyes", contents);
		while(iter.hasNext()){
			String label = iter.next();
			sigBuilder.update(label, iter.get(label));
		}
		CryptoSignature sig = sigBuilder.build();
		
		JSONBuilder builder = new JSONBuilder("Chinese Eyes");
		builder.add(contact);
		builder.add(contents);
		builder.add(sig);
		
		StringWriter writer = new StringWriter();
		builder.format(writer);
		System.err.println(writer.toString());
		
	}

}
