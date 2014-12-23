package com.cryptoregistry.formats;

import java.io.StringWriter;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.MapData;
import com.cryptoregistry.ListData;
import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.builder.ContactContentsIterator;
import com.cryptoregistry.signature.builder.MapDataContentsIterator;
import com.cryptoregistry.signature.builder.ECKeyContentsIterator;
import com.cryptoregistry.signature.builder.ECDSASignatureBuilder;
import com.cryptoregistry.signature.builder.ListDataContentsIterator;
import com.cryptoregistry.util.MapIterator;


public class ECDSAFormattingTest {

	@Test
	public void test0() {
		
		final String curveName = "P-256";
		
		CryptoContact contact = new CryptoContact();
		contact.add("GivenName.0", "David");
		contact.add("GivenName.1", "Richard");
		contact.add("FamilyName.0", "Smith");
		contact.add("Address.0", "1714 Roberts Ct.");
		contact.add("City", "Madison");
		contact.add("State", "Wisconsin");
		contact.add("PostalCode", "53711");
		contact.add("CountryCode", "US");
		
		MapData ld = new MapData();
		ld.put("Copyright", "\u00A9 2014, David R. Smith. All Rights Reserved");
		ld.put("License", "http://www.apache.org/licenses/LICENSE-2.0.txt");
		
		ListData rd = new ListData();
		rd.addURL("http://buttermilk.googlecode.com/svn/trunk/buttermilk-core/data/test0.json");
		
		ECKeyContents contents = CryptoFactory.INSTANCE.generateKeys(curveName);
		MapIterator iter = new ECKeyContentsIterator(contents);
		MapIterator iter2 = new ContactContentsIterator(contact);
		MapIterator iter3 = new MapDataContentsIterator(ld);
		ListDataContentsIterator remoteIter = new ListDataContentsIterator(rd);
		ECDSASignatureBuilder sigBuilder = new ECDSASignatureBuilder("Chinese Eyes", contents);
		
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
			List<MapData> list = remoteIter.nextData();
			for(MapData data: list){
				MapIterator inner = new MapDataContentsIterator(data);
				while(inner.hasNext()){
					String label = inner.next();
					sigBuilder.update(label, inner.get(label));
				}
			}
		}
		
		CryptoSignature sig = sigBuilder.build();
		
		JSONFormatter builder = new JSONFormatter("Chinese Eyes");
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
	
	@Test
	public void test1(){
		char [] password = "password1".toCharArray();
		ECKeyContents keys0 = CryptoFactory.INSTANCE.generateKeys("P-256");
		String handle = keys0.getMetadata().getHandle();
		JSONFormatter format = new JSONFormatter("Chinese Knees");
	    format.add(keys0); // formats an unsecured key
	    format.add(keys0.clone(new KeyFormat(password))); // formats a secured clone of the key with a Base64url encoding hint, which is right for Curve25519
	    format.add(keys0.cloneForPublication()); // makes a clone ready for publication
		StringWriter writer = new StringWriter();
		format.format(writer);
		String out = writer.toString();
		Assert.assertTrue(out.indexOf(handle+"-U")>0);
		Assert.assertTrue(out.indexOf(handle+"-S")>0);
		Assert.assertTrue(out.indexOf(handle+"-P")>0);
		//System.err.println(out);
	}

}
