package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.proto.builder.ECKeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;

/**
 * Used with the Handshake contentType. 
 * 
 * @author Dave
 *
 */
public class ECKeyForPublicationOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final ECKeyForPublication keyContents;
	
	public ECKeyForPublicationOutputFrame(byte contentType, ECKeyForPublication keyContents) {
		this.keyContents = keyContents; 
		this.contentType = contentType;
	}

	@Override
	public void writeFrame(OutputStream stream) {
		ECKeyForPublicationProtoBuilder builder = new ECKeyForPublicationProtoBuilder(keyContents);
		ECKeyForPublicationProto proto = builder.build();
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
