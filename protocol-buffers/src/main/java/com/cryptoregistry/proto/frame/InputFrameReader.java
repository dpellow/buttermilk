package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.InputStream;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.proto.reader.C2KeyContentsProtoReader;
import com.cryptoregistry.proto.reader.SecureMessageProtoReader;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.SecureBytesProto;
import com.google.protobuf.ByteString;

public class InputFrameReader extends InputFrameBase {

	public InputFrameReader() {}

	public Curve25519KeyForPublication readC2KeyContents(InputStream in){
		try {
			int sz = this.readInt(in);
			byte [] b = new byte[sz];
			this.readFully(in, b, 0, sz);
			C2KeyContentsProto proto = C2KeyContentsProto.parseFrom(ByteString.copyFrom(b));
			C2KeyContentsProtoReader reader = new C2KeyContentsProtoReader(proto);
			return reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public SecureMessage readSecureMessage(InputStream in){
		try {
			int sz = this.readInt(in);
			byte [] b = new byte[sz];
			this.readFully(in, b, 0, sz);
			SecureBytesProto proto = SecureBytesProto.parseFrom(ByteString.copyFrom(b));
			SecureMessageProtoReader reader = new SecureMessageProtoReader(proto);
			return reader.read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
