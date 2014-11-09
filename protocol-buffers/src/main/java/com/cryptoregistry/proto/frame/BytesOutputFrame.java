package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.builder.BytesProtoBuilder;
import com.cryptoregistry.protos.Buttermilk.BytesProto;

public class BytesOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte[] data;
	
	public BytesOutputFrame(SensitiveBytes sm) {
		this.data = sm.getData();
	}
	
	public BytesOutputFrame(byte[] sm) {
		this.data = sm;
	}

	@Override
	public void writeFrame(OutputStream out) {
		BytesProtoBuilder builder = new BytesProtoBuilder(data);
		BytesProto proto = builder.build();
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
