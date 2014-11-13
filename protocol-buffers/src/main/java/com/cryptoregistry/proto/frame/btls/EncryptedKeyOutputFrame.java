package com.cryptoregistry.proto.frame.btls;

import java.io.IOException;
import java.io.OutputStream;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.builder.BytesProtoBuilder;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.OutputFrameBase;
import com.cryptoregistry.protos.Buttermilk.BytesProto;

/**
 * Used with the Handshake contentType
 * 
 * @author Dave
 *
 */
public class EncryptedKeyOutputFrame extends OutputFrameBase implements OutputFrame {

	final byte contentType;
	final byte[] data;
	final byte [] iv;
	
	public EncryptedKeyOutputFrame(byte contentType, SensitiveBytes sm, byte[]iv) {
		this.contentType = contentType;
		this.data = sm.getData();
		this.iv = iv;
	}
	
	public EncryptedKeyOutputFrame(byte contentType, byte[] sm, byte[]iv) {
		this.contentType = contentType;
		this.data = sm;
		this.iv =iv;
	}

	@Override
	public void writeFrame(OutputStream out) {
		BytesProtoBuilder builder = new BytesProtoBuilder(data);
		BytesProto proto = builder.build();
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
