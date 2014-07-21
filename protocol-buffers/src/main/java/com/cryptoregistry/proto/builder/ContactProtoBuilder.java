package com.cryptoregistry.proto.builder;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.protos.Buttermilk;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
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
		
		MapProtoBuilder mBuilder = new MapProtoBuilder(contact.getMap());
		
		NamedMapProto.Builder namedMapProtoBuilder = NamedMapProto.newBuilder();
		namedMapProtoBuilder.setMap(mBuilder.build()).setUuid(handle);
		
		return Buttermilk.CryptoContactProto.newBuilder().setMap(namedMapProtoBuilder.build()).build();
	}
	
	public byte [] buildToBytes() {
		return build().toByteArray();
	}

}
