package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.StringProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.StringProto;

public class AlertOutputFrame extends OutputFrameBase implements OutputFrame {

	final String data; // assumed to be UTF-8
	final byte contentType; // code for this message type
	
	public AlertOutputFrame(byte contentType, String sm) {
		this.contentType = contentType;
		this.data = sm;
	}

	@Override
	public void writeFrame(OutputStream out) {
		StringProtoBuilder builder = new StringProtoBuilder(data);
		StringProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int sz = bytes.length;
		try {
			this.writeByte(out, contentType);
			this.writeInt(out, sz);
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
