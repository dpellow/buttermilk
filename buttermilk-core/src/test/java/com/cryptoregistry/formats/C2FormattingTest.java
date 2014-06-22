package com.cryptoregistry.formats;

import java.io.StringWriter;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.LocalData;
import com.cryptoregistry.RemoteData;
import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.builder.C2KeyContentsIterator;
import com.cryptoregistry.signature.builder.C2SignatureBuilder;
import com.cryptoregistry.signature.builder.ContactContentsIterator;
import com.cryptoregistry.signature.builder.LocalDataContentsIterator;
import com.cryptoregistry.signature.builder.RemoteDataContentsIterator;
import com.cryptoregistry.util.MapIterator;


public class C2FormattingTest {

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
		
		LocalData ld = new LocalData();
		ld.put("Copyright", "\u00A9 2014, David R. Smith. All Rights Reserved");
		ld.put("License", "http://www.apache.org/licenses/LICENSE-2.0.txt");
		
		RemoteData rd = new RemoteData();
		rd.addURL("http://buttermilk.googlecode.com/svn/trunk/buttermilk-core/data/test0.json");
		
		Curve25519KeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		MapIterator iter = new C2KeyContentsIterator(contents);
		MapIterator iter2 = new ContactContentsIterator(contact);
		MapIterator iter3 = new LocalDataContentsIterator(ld);
		RemoteDataContentsIterator remoteIter = new RemoteDataContentsIterator(rd);
		C2SignatureBuilder sigBuilder = new C2SignatureBuilder("Chinese Eyes", contents);
		
		while(iter.hasNext()){
			String label = iter.next();
			sigBuilder.update(label, iter.get(label));
		}
		while(iter2.hasNext()){
			String label = iter2.next();
			sigBuilder.update(label, iter2.get(label));
		}
		while(iter3.hasNext()){
			String label = iter3.next();
			sigBuilder.update(label, iter3.get(label));
		}
		
		// a bit more complex - the remote iterator outputs a list resolved into LocalData
		while(remoteIter.hasNext()){
			List<LocalData> list = remoteIter.nextData();
			for(LocalData data: list){
				MapIterator inner = new LocalDataContentsIterator(data);
				while(inner.hasNext()){
					String label = inner.next();
					sigBuilder.update(label, inner.get(label));
				}
			}
		}
		
		CryptoSignature sig = sigBuilder.build();
		
		JSONBuilder builder = new JSONBuilder("Chinese Eyes");
		builder.add(contact)
		.add(contents)
		.add(sig)
		.add(ld)
		.add(rd);
		
		StringWriter writer = new StringWriter();
		builder.format(writer);
		String output = writer.toString();
		
		Assert.assertTrue(output != null);
		
		System.err.println(output);
		
	}

}
