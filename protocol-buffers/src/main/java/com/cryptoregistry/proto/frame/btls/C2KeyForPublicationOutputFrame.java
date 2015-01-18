/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.proto.builder.C2KeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;

/**
 * Used with the Handshake contentType. 
 * 
 * @author Dave
 *
 */
public class C2KeyForPublicationOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final int subcode;
	final Curve25519KeyForPublication keyContents;
	
	public C2KeyForPublicationOutputFrame(int contentType, int subcode, Curve25519KeyForPublication keyContents) {
		this.keyContents = keyContents; 
		this.contentType = (byte) contentType;
		this.subcode = subcode;
	}

	@Override
	public void writeFrame(OutputStream out) {
		C2KeyForPublicationProtoBuilder builder = new C2KeyForPublicationProtoBuilder(keyContents);
		C2KeyForPublicationProto proto = builder.build();
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
