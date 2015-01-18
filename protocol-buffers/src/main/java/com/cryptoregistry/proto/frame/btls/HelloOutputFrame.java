package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.HelloProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.HelloProto;


public class HelloOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final int subcode;
	final String regHandle; // assumed to be UTF-8
	final String keyHandle; // assumed to be UTF-8

	public HelloOutputFrame(int contentType, int subcode, String regHandle, String keyHandle) {
		super();
		this.contentType = (byte) contentType;
		this.regHandle = regHandle;
		this.keyHandle = keyHandle;
		this.subcode = subcode; 
	}

	@Override
	public void writeFrame(OutputStream out) {
		HelloProtoBuilder builder = new HelloProtoBuilder(regHandle,keyHandle);
		HelloProto proto = builder.build();
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
