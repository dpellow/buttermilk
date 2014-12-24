package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.RSAKeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.rsa.RSAKeyForPublication;

/**
 * Used with the Handshake contentType. 
 * 
 * @author Dave
 *
 */
public class RSAKeyForPublicationOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final RSAKeyForPublication keyContents;
	
	public RSAKeyForPublicationOutputFrame(byte contentType, RSAKeyForPublication keyContents) {
		this.keyContents = keyContents; 
		this.contentType = contentType;
	}

	@Override
	public void writeFrame(OutputStream stream) {
		RSAKeyForPublicationProtoBuilder builder = new RSAKeyForPublicationProtoBuilder(keyContents);
		RSAKeyContentsProto proto = builder.build();
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
