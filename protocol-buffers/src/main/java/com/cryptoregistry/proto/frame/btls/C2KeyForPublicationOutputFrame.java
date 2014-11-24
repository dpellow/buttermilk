package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.proto.builder.C2KeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;

/**
 * Used with the Handshake contentType. 
 * 
 * @author Dave
 *
 */
public class C2KeyForPublicationOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final Curve25519KeyForPublication keyContents;
	
	public C2KeyForPublicationOutputFrame(Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents;
		contentType = (byte)22; // handshake
	}
	
	public C2KeyForPublicationOutputFrame(byte contentType, Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents; 
		this.contentType = contentType;
	}

	@Override
	public void writeFrame(OutputStream stream) {
		C2KeyForPublicationProtoBuilder builder = new C2KeyForPublicationProtoBuilder(keyContents);
		C2KeyForPublicationProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int size = bytes.length;
		try {
			writeByte(stream, contentType);
			writeInt(stream,size);
			stream.write(bytes);
			stream.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
