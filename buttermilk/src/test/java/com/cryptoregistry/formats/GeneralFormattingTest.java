package com.cryptoregistry.formats;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.FileURLResolver;
import com.cryptoregistry.HTTPURLResolver;
import com.cryptoregistry.LocalData;
import com.cryptoregistry.RemoteData;
import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.builder.ContactContentsIterator;
import com.cryptoregistry.signature.builder.LocalDataContentsIterator;
import com.cryptoregistry.signature.builder.RSAKeyContentsIterator;
import com.cryptoregistry.signature.builder.RSASignatureBuilder;
import com.cryptoregistry.signature.builder.RemoteDataContentsIterator;
import com.cryptoregistry.util.MapIterator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

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
		
		LocalData ld = new LocalData();
		ld.put("Copyright", "\u00A9 2014, David R. Smith. All Rights Reserved");
		ld.put("License", "http://www.apache.org/licenses/LICENSE-2.0.txt");
		
		RemoteData rd = new RemoteData();
		rd.addURL("http://buttermilk.googlecode.com/svn/trunk/buttermilk/data/test0.json");
		
		RSAKeyContents contents = CryptoFactory.INSTANCE.generateKeys();
		MapIterator iter = new RSAKeyContentsIterator(contents);
		MapIterator iter2 = new ContactContentsIterator(contact);
		MapIterator iter3 = new LocalDataContentsIterator(ld);
		RemoteDataContentsIterator remoteIter = new RemoteDataContentsIterator(rd);
		RSASignatureBuilder sigBuilder = new RSASignatureBuilder("Chinese Eyes", contents);
		
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
		System.err.println(writer.toString());
		
	}
	
	@Test
	public void test1() throws IOException {
		
		LocalData ld0 = new LocalData();
		ld0.put("Copyright", "\u00A9 2014, David R. Smith. All Rights Reserved");
		ld0.put("License", "http://www.apache.org/licenses/LICENSE-2.0.txt");
		
		LocalData ld1 = new LocalData();
		ld1.put("some key", "some data");
		ld1.put("another key", "more data");
		LocalDataFormatter format = new LocalDataFormatter();
		format.add(ld0);
		format.add(ld1);
		
		StringWriter writer = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
				format.format(g, writer);
			g.writeEndObject();
		}finally{
			g.close();
		}
		
		System.err.println(writer.toString());
		
		MapIterator iter3 = new LocalDataContentsIterator(ld0);
		while(iter3.hasNext()){
			String label = iter3.next();
			Assert.assertNotNull(iter3.get(label));
		}
		
		
	}
	
	@Test
	public void test2() throws IOException {
		
		String raw = "data/test0.json";
		File f = new File(raw);
		String fileUrl = f.toURI().toURL().toExternalForm();
		
		FileURLResolver fur = new FileURLResolver(fileUrl);
		List<LocalData> data = fur.resolve();
		Assert.assertTrue(data != null);
		Assert.assertTrue(data.size() == 2);
		
		fur = new FileURLResolver(fileUrl);
		data = fur.resolve();
		Assert.assertTrue(data != null);
		Assert.assertTrue(data.size() == 2);
		
		HTTPURLResolver hur = new HTTPURLResolver();
		hur.setUrl("http://buttermilk.googlecode.com/svn/trunk/buttermilk/data/test0.json");
		
		data = hur.resolve();
		Assert.assertTrue(data != null);
		Assert.assertTrue(data.size() == 2);
		
	}

}
