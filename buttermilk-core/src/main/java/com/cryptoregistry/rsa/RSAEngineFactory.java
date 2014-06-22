/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.rsa;

import x.org.bouncycastle.crypto.digests.MD5Digest;
import x.org.bouncycastle.crypto.digests.SHA1Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.crypto.digests.SHA512Digest;
import x.org.bouncycastle.crypto.encodings.OAEPEncoding;
import x.org.bouncycastle.crypto.encodings.ISO9796d1Encoding;
import x.org.bouncycastle.crypto.encodings.PKCS1Encoding;
import x.org.bouncycastle.crypto.engines.RSABlindedEngine;
import x.org.bouncycastle.crypto.AsymmetricBlockCipher;

public class RSAEngineFactory {

	private String paddingScheme;
	
	public RSAEngineFactory(String paddingScheme) {
		this.paddingScheme = paddingScheme.toUpperCase();
	}
	
	public AsymmetricBlockCipher getCipher() {
		switch(paddingScheme){
			case "NOPADDING":{
				return new RSABlindedEngine();
			}
			case "PKCS1PADDING":{
				return new PKCS1Encoding(new RSABlindedEngine());
			}
			case "ISO97961PADDING":
			case "ISO9796-1PADDING":{
				return new ISO9796d1Encoding(new RSABlindedEngine());
			}
			case "OAEPWITHMD5ANDMGF1PADDING":{
				return new OAEPEncoding(
						new RSABlindedEngine(), 
						new MD5Digest(), 
						new byte[0]);
			}
			case "OAEPPADDING":
			case "OAEPWITHSHA1ANDMGF1PADDING":
			case "OAEPWITHSHA-1ANDMGF1PADDING":{
				return new OAEPEncoding(
						new RSABlindedEngine(), 
						new SHA1Digest(), 
						new byte[0]);
			}
			case "OAEPWITHSHA256ANDMGF1PADDING":
			case "OAEPWITHSHA-256ANDMGF1PADDING":{
				return new OAEPEncoding(
						new RSABlindedEngine(), 
						new SHA256Digest(), 
						new byte[0]);
			}
			case "OAEPWITHSHA512ANDMGF1PADDING":
			case "OAEPWITHSHA-512ANDMGF1PADDING":{
				return new OAEPEncoding(
						new RSABlindedEngine(), 
						new SHA512Digest(), 
						new byte[0]);
			}
			default: throw new RuntimeException(paddingScheme + " unavailable with RSA.");
		}
	}
	
	enum Padding {
		NOPADDING,
		PKCS1PADDING,
		ISO97961PADDING,
		OAEPWITHMD5ANDMGF1PADDING,
		OAEPPADDING,
		OAEPWITHSHA256ANDMGF1PADDING,
		OAEPWITHSHA512ANDMGF1PADDING;
	}

	public String getPaddingScheme() {
		return paddingScheme;
	}

}