package com.cryptoregistry.proto.builder;

import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingHintProto;

public class KeyMetadataProtoBuilder {

	final CryptoKeyMetadata meta;
	
	public KeyMetadataProtoBuilder(CryptoKeyMetadata meta) {
		this.meta = meta;
	}
	
	public KeyMetadataProto build() {
		
		EncodingHintProto enc = null;
		EncodingHint e = meta.getFormat().encodingHint;
		switch(e){
			case NoEncoding: enc = EncodingHintProto.NOENCODING; break;
			case RawBytes: enc = EncodingHintProto.RAWBYTES; break;
			case Base2: enc = EncodingHintProto.BASE2; break;
			case Base10: enc = EncodingHintProto.BASE10; break;
			case Base16: enc = EncodingHintProto.BASE16; break;
			case Base64: enc = EncodingHintProto.BASE64; break;
			case Base64url: enc = EncodingHintProto.BASE64URL; break;
			default: throw new RuntimeException("Unknown: "+e);
		}
		
		KeyMetadataProto metaDataProto = KeyMetadataProto.newBuilder()
		.setHandle(meta.getHandle())
		.setCreatedOn(meta.getCreatedOn().getTime())
		.setKeyGenerationAlgorithm(meta.getKeyAlgorithm().toString())
		.setEncodingHint(enc)
		.build();
		
		return metaDataProto;
	}

}
