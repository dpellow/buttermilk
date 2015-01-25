/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.proto.builder.HandshakeDigestProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.HandshakeDigestProto;


public class HandshakeDigestOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final int subcode;
	final byte [] digest; 

	public HandshakeDigestOutputFrame(int contentType, int subcode, byte [] digest) {
		super();
		this.contentType = (byte) contentType;
		this.digest = digest;
		this.subcode = subcode; 
	}

	@Override
	public void writeFrame(OutputStream out) {
		HandshakeDigestProtoBuilder builder = new HandshakeDigestProtoBuilder(digest);
		HandshakeDigestProto proto = builder.build();
		byte [] bytes = proto.toByteArray();
		int sz = bytes.length;
		try {
			this.writeByte(out, contentType);
			this.writeShort(out, subcode);
			this.writeShort(out, sz);
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
