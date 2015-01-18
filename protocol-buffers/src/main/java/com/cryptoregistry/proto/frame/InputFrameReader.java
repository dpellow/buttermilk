package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.InputStream;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.proto.reader.C2KeyForPublicationProtoReader;
import com.cryptoregistry.protos.Buttermilk.BytesProto;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.StringProto;
import com.google.protobuf.ByteString;

public class InputFrameReader extends InputFrameBase {

	public InputFrameReader() {}
	
	

	public Curve25519KeyForPublication readC2KeyForPublication(InputStream in){
		try {
			int sz = this.readInt32(in);
			byte [] b = new byte[sz];
			this.readFully(in, b, 0, sz);
			C2KeyForPublicationProto proto = C2KeyForPublicationProto.parseFrom(ByteString.copyFrom(b));
			C2KeyForPublicationProtoReader reader = new C2KeyForPublicationProtoReader(proto);
			return reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public byte [] readBytesProto(InputStream in){
		try {
			int sz = this.readInt32(in);
			byte [] b = new byte[sz];
			this.readFully(in, b, 0, sz);
			BytesProto proto = BytesProto.parseFrom(ByteString.copyFrom(b));
			return proto.getData().toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String readStringProto(InputStream in){
		try {
			int sz = this.readInt32(in);
			byte [] b = new byte[sz];
			this.readFully(in, b, 0, sz);
			StringProto proto = StringProto.parseFrom(ByteString.copyFrom(b));
			return proto.getData();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
