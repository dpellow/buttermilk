/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.compat;

import com.cryptoregistry.formats.EncodingHint;
import com.cryptoregistry.protos.Buttermilk.KeyMetadataProto.EncodingHintProto;

public class EncodingAdapter {

	public static EncodingHintProto getProtoFor(EncodingHint enc){
		switch(enc){
		case NoEncoding: return EncodingHintProto.NOENCODING;
		case RawBytes: return EncodingHintProto.RAWBYTES;
		case Base2: return EncodingHintProto.BASE2;
		case Base10: return EncodingHintProto.BASE10;
		case Base16: return EncodingHintProto.BASE16;
		case Base64: return EncodingHintProto.BASE64;
		case Base64url: return EncodingHintProto.BASE64URL;
			default: { throw new RuntimeException("Encoding not found: "+enc);}
		}
	}
	
	public static EncodingHint getEncodingFor(EncodingHintProto enc){
		switch(enc){
		case NOENCODING: return EncodingHint.NoEncoding;
		case RAWBYTES: return EncodingHint.RawBytes;
		case BASE2: return EncodingHint.Base2;
		case BASE10: return EncodingHint.Base10;
		case BASE16: return EncodingHint.Base16;
		case BASE64: return EncodingHint.Base64;
		case BASE64URL: return EncodingHint.Base64url;
			default: { throw new RuntimeException("Encoding not found: "+enc);}
		}
	}

}
