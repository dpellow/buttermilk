package com.cryptoregistry.proto.frame;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.proto.builder.SecureBytesProtoBuilder;
import com.cryptoregistry.protos.Buttermilk.SecureBytesProto;

public class SecureMessageOutputFrame extends OutputFrameBase implements OutputFrame {

	final SecureMessage secureMessage;
	
	public SecureMessageOutputFrame(SecureMessage sm) {
		this.secureMessage = sm;
	}

	@Override
	public void writeFrame(OutputStream out) {
		SecureBytesProtoBuilder builder = new SecureBytesProtoBuilder(secureMessage);
		SecureBytesProto proto = builder.build();
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
