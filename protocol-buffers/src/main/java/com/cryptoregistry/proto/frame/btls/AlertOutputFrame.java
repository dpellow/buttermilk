package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.AuthenticatedStringProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.AuthenticatedStringProto;

public class AlertOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType; // code for this message type, always BTLSProtocol.ALERT
	final short subcode; // subcode, something specific to the alert
	final String data; // assumed to be UTF-8, human readable message
	final byte [] hmac;
	
	
	public AlertOutputFrame(int contentType, int subcode,  String msg, byte [] hmac) {
		this.contentType = (byte) contentType;
		this.subcode = (short) subcode;
		this.data = msg;
		this.hmac = hmac;
	}

	@Override
	public void writeFrame(OutputStream out) {
		AuthenticatedStringProtoBuilder builder = new AuthenticatedStringProtoBuilder(data,hmac);
		AuthenticatedStringProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int sz = bytes.length;
		try {
			this.writeByte(out, contentType);
			this.writeShort(out, subcode);
			this.writeShort(out, sz); // TODO validate sz, length cannot exceed 32767
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
