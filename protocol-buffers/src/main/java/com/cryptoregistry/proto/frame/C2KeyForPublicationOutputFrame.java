package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.proto.builder.C2KeyForPublicationProtoBuilder;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;

public class C2KeyForPublicationOutputFrame implements OutputFrame {

	final Curve25519KeyForPublication keyContents;
	
	public C2KeyForPublicationOutputFrame(Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents; 
	}

	@Override
	public void writeFrame(OutputStream stream) {
		C2KeyForPublicationProtoBuilder builder = new C2KeyForPublicationProtoBuilder(keyContents);
		C2KeyForPublicationProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int size = bytes.length;
		try {
			writeInt(stream,size);
			stream.write(bytes);
			stream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public final void writeInt(OutputStream out, int v) throws IOException {
        out.write((v >>> 24) & 0xFF);
        out.write((v >>> 16) & 0xFF);
        out.write((v >>>  8) & 0xFF);
        out.write((v >>>  0) & 0xFF);
        out.flush();
    }
}
