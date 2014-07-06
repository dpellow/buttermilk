package com.cryptoregistry.proto.builder;

import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.protos.Buttermilk;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
import com.cryptoregistry.protos.Buttermilk.EntryProto;
import com.cryptoregistry.protos.Buttermilk.MapProto;
import com.cryptoregistry.protos.Buttermilk.NamedMapProto;

/**
 * Given a CryptoContact, build a protocol buffer from it
 * 
 * @author Dave
 *
 */
public class ContactProtoBuilder {

	final CryptoContact contact;
	
	public ContactProtoBuilder(CryptoContact contact) {
		this.contact = contact;
	}
	
	public CryptoContactProto build() {
		String handle = contact.getHandle();
		
		// create the map proto
		Map<String,String> map = contact.getMap();
		MapProto.Builder mapProtoBuilder = MapProto.newBuilder();
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			String value = map.get(key);
			EntryProto entry = EntryProto.newBuilder().setKey(key).setValue(value).build();
			mapProtoBuilder.addEntries(entry);
		}
		
		NamedMapProto.Builder namedMapProtoBuilder = NamedMapProto.newBuilder();
		namedMapProtoBuilder.setMap(mapProtoBuilder.build()).setUuid(handle);
		
		return Buttermilk.CryptoContactProto.newBuilder().setMap(namedMapProtoBuilder.build()).build();
	}
	
	public byte [] buildToBytes() {
		return build().toByteArray();
	}

}
