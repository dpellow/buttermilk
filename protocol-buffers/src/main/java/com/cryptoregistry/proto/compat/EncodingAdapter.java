package com.cryptoregistry.proto.compat;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingProto;

public class EncodingAdapter {

	public static EncodingProto getProtoFor(Encoding enc){
		switch(enc){
		case NoEncoding: return EncodingProto.NOENCODING;
		case RawBytes: return EncodingProto.RAWBYTES;
		case Base2: return EncodingProto.BASE2;
		case Base10: return EncodingProto.BASE10;
		case Base16: return EncodingProto.BASE16;
		case Base64: return EncodingProto.BASE64;
		case Base64url: return EncodingProto.BASE64URL;
			default: { throw new RuntimeException("Encoding not found: "+enc);}
		}
	}
	
	public static Encoding getEncodingFor(EncodingProto enc){
		switch(enc){
		case NOENCODING: return Encoding.NoEncoding;
		case RAWBYTES: return Encoding.RawBytes;
		case BASE2: return Encoding.Base2;
		case BASE10: return Encoding.Base10;
		case BASE16: return Encoding.Base16;
		case BASE64: return Encoding.Base64;
		case BASE64URL: return Encoding.Base64url;
			default: { throw new RuntimeException("Encoding not found: "+enc);}
		}
	}

}
