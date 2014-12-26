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
	final int subcode;
	final ECKeyForPublication keyContents;
	
	public ECKeyForPublicationOutputFrame(byte contentType, int subcode, ECKeyForPublication keyContents) {
		this.keyContents = keyContents; 
		this.contentType = contentType;
		this.subcode = subcode;
	}

	@Override
	public void writeFrame(OutputStream out) {
		ECKeyForPublicationProtoBuilder builder = new ECKeyForPublicationProtoBuilder(keyContents);
		ECKeyForPublicationProto proto = builder.build();
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
