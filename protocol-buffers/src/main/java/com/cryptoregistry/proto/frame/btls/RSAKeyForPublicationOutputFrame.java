package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.RSAKeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.RSAKeyForPublicationProto;
import com.cryptoregistry.rsa.RSAKeyForPublication;

/**
 * Used with the Handshake contentType. 
 * 
 * @author Dave
 *
 */
public class RSAKeyForPublicationOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final int subcode;
	final RSAKeyForPublication keyContents;
	
	public RSAKeyForPublicationOutputFrame(byte contentType, int subcode, RSAKeyForPublication keyContents) {
		this.keyContents = keyContents; 
		this.contentType = contentType;
		this.subcode = subcode;
	}

	@Override
	public void writeFrame(OutputStream out) {
		RSAKeyForPublicationProtoBuilder builder = new RSAKeyForPublicationProtoBuilder(keyContents);
		RSAKeyForPublicationProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int sz = bytes.length;
		try {
			this.writeByte(out, contentType);
			this.writeShort(out, subcode);
			this.writeShort(out, sz); // TODO validate sz, length cannot exceed 32767
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
