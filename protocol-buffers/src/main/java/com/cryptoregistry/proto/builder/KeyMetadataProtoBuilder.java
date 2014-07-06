package com.cryptoregistry.proto.builder;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingProto;

public class KeyMetadataProtoBuilder {

	final CryptoKeyMetadata meta;
	
	public KeyMetadataProtoBuilder(CryptoKeyMetadata meta) {
		this.meta = meta;
	}
	
	public KeyMetadataProto build() {
		
		EncodingProto enc = null;
		Encoding e = meta.getFormat().encoding;
		switch(e){
			case NoEncoding: enc = EncodingProto.NOENCODING; break;
			case RawBytes: enc = EncodingProto.RAWBYTES; break;
			case Base2: enc = EncodingProto.BASE2; break;
			case Base10: enc = EncodingProto.BASE10; break;
			case Base16: enc = EncodingProto.BASE16; break;
			case Base64: enc = EncodingProto.BASE64; break;
			case Base64url: enc = EncodingProto.BASE64URL; break;
			default: throw new RuntimeException("Unknown: "+e);
		}
		
		KeyMetadataProto metaDataProto = KeyMetadataProto.newBuilder()
		.setHandle(meta.getHandle())
		.setCreatedOn(meta.getCreatedOn().getTime())
		.setEncoding(enc)
		.build();
		
		return metaDataProto;
	}

}
