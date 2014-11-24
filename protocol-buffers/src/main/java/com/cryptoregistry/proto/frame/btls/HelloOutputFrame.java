package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.HelloProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.HelloProto;


public class HelloOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final String handle; // assumed to be UTF-8
	final String keyHandle; // assumed to be UTF-8

	public HelloOutputFrame(byte contentType, String handle, String keyHandle) {
		super();
		this.contentType = contentType;
		this.handle = handle;
		this.keyHandle = keyHandle;
	}

	@Override
	public void writeFrame(OutputStream out) {
		HelloProtoBuilder builder = new HelloProtoBuilder(handle,keyHandle);
		HelloProto proto = builder.build();
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
