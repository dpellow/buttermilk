package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.SessionDetailsProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.SessionDetailsProto;

/**
 * Used with the Handshake contentType to send an encapsulated ephemeral key
 * 
 * @author Dave
 *
 */
public class SessionDetailsOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final int subcode;
	final String sessionId; // id associated with this key encapsulation
	final byte[] sessionKey; // encrypted bytes
	final String sessionSymmetricAlg; // what was used for the key encapsulation
	final byte [] iv; // optional, depends on alg used for encrypting sessionKey

	

	public SessionDetailsOutputFrame(byte contentType, int subcode,
			String sessionId, byte[] sessionKey, String sessionSymmetricAlg,
			byte[] iv) {
		super();
		this.contentType = contentType;
		this.subcode = subcode;
		this.sessionId = sessionId;
		this.sessionKey = sessionKey;
		this.sessionSymmetricAlg = sessionSymmetricAlg;
		this.iv = iv;
	}

	@Override
	public void writeFrame(OutputStream out) {
		SessionDetailsProtoBuilder builder = new SessionDetailsProtoBuilder(
				sessionId, 
				sessionKey,
				sessionSymmetricAlg,
				iv);
		SessionDetailsProto proto = builder.build();
		
		byte [] bytes = proto.toByteArray();
		int sz = bytes.length;
		try {
			this.writeByte(out, contentType); // message type
			this.writeShort(out, subcode);
			this.writeShort(out, sz); // length of remaining message after this
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
