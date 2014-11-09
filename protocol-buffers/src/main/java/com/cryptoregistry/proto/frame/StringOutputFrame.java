package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.StringProtoBuilder;
import com.cryptoregistry.protos.Buttermilk.StringProto;

public class StringOutputFrame extends OutputFrameBase implements OutputFrame {

	final String data;
	
	public StringOutputFrame(String sm) {
		this.data = sm;
	}

	@Override
	public void writeFrame(OutputStream out) {
		StringProtoBuilder builder = new StringProtoBuilder(data);
		StringProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int sz = bytes.length;
		try {
			this.writeInt(out, sz);
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
