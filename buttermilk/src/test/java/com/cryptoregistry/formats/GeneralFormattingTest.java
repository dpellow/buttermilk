package com.cryptoregistry.formats;

import java.nio.charset.Charset;

import junit.framework.Assert;
import x.org.bouncycastle.crypto.digests.SHA256Digest;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.signature.RSACryptoSignature;
import com.cryptoregistry.signature.builder.RSAKeySignatureBuilder;

public class GeneralFormattingTest {

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
		
		RSAKeySignatureBuilder sigBuilder = new RSAKeySignatureBuilder("Chinese Eyes", contents);
	//	sigBuilder.update(label, bytes);
		
		
		
		
		JSONBuilder builder = new JSONBuilder("Chinese Eyes");
		builder.add(contact);
		builder.add(contents);
		//builder.add(sig);
		
		
	}

}
