/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.SecureMessageService;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.reader.SecureMessageProtoReader;
import com.cryptoregistry.protos.Buttermilk.SecureMessageProto;

public class SecureMessageInputStream extends FilterInputStream {
	
	// symmetric key
	SensitiveBytes key;

	protected SecureMessageInputStream(SensitiveBytes key, InputStream in) {
		super(in);
		this.key = key;
	}

	// will return a String, char array or byte array based on the encapsulated input hint
	public Object readSecureMessage() throws IOException {
			SecureMessageProto proto = SecureMessageProto.parseFrom(in);
			SecureMessageProtoReader reader = new SecureMessageProtoReader(proto);
			SecureMessage msg = reader.read();
			SecureMessageService service = new SecureMessageService(key.getData(),msg);
			service.decrypt();
			return msg.result();
	}

}
