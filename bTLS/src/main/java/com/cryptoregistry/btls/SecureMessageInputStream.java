/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.cryptoregistry.crypto.mt.SecureMessage;
import com.cryptoregistry.crypto.mt.SecureMessageService;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.frame.InputFrameReader;

public class SecureMessageInputStream extends FilterInputStream {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.SecureMessageInputStream");
	
	// symmetric key
	SensitiveBytes key;

	protected SecureMessageInputStream(SensitiveBytes key, InputStream in) {
		super(in);
		this.key = key;
	}

	public byte[] readSecureMessage() throws IOException {
		log.trace("reading secure message");
		InputFrameReader reader = new InputFrameReader();
		SecureMessage sm = reader.readSecureMessage(in);
		//sm.rotate();
		SecureMessageService srv = new SecureMessageService(key.getData(), sm);
		srv.decrypt();
		return sm.byteResult();
	}

	public String readSecureMessageAsUTF8String() throws IOException {
		InputFrameReader reader = new InputFrameReader();
		SecureMessage sm = reader.readSecureMessage(in);
		//sm.rotate();
		SecureMessageService srv = new SecureMessageService(key.getData(), sm);
		srv.decrypt();
		return sm.stringResult();
	}
	
	

}
