/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.signature.builder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import x.org.bouncycastle.crypto.Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;

import com.cryptoregistry.SignatureAlgorithm;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.signature.ECDSACryptoSignature;
import com.cryptoregistry.signature.SignatureMetadata;


/**<pre>
 * 
 * construct a signature using ECDSA. Basic process:
 * 
 * builder(registration handle, signer key contents, digest)
 * builder.update("handle:token0", bytes0)
 * builder.update("handle:token1", bytes1)
 * builder.update("handle:token2", bytes2)
 * RSACryptoSignature sigHolder = builder.build()
 * 
 * however, you can make use of the various content iterators to make signing much easier. See test cases
 * 
 * 
 * 
 * </pre>
 * @author Dave
 *
 */
public class ECDSASignatureBuilder {

	final ECKeyContents sKey;
	final Digest digest;
	final List<String> references;
	final String signedBy;
	final SignatureMetadata meta;
	
	public ECDSASignatureBuilder(String signedBy, ECKeyContents sKey, Digest digest) {
		super();
		this.sKey = sKey;
		this.digest = digest;
		this.references=new ArrayList<String>();
		this.signedBy = signedBy;
		if(signedBy == null) throw new RuntimeException("Registration Handle cannot be null");
		
		meta = new SignatureMetadata(
				UUID.randomUUID().toString(),
				new Date(),
				SignatureAlgorithm.RSA,
				digest.getAlgorithmName(),
				sKey.getHandle(),
				signedBy);
		update(meta.getHandle()+":SignedBy",signedBy);
		update(".SignedWith",sKey.getHandle());
		
	}
	
	/**
	 * By default this constructor updates SignedBy and SignedWith, so even with no other
	 * calls to update you get a meaningful signature out of build()
	 *  
	 * @param signedBy
	 * @param sKey
	 */
	public ECDSASignatureBuilder(String signedBy, ECKeyContents sKey) {
		super();
		this.sKey = sKey;
		this.digest = new SHA256Digest();
		this.references=new ArrayList<String>();
		this.signedBy = signedBy;
		if(signedBy == null) throw new RuntimeException("Registration Handle cannot be null");
		meta = new SignatureMetadata(
				UUID.randomUUID().toString(),
				new Date(),
				SignatureAlgorithm.ECDSA,
				digest.getAlgorithmName(),
				sKey.getHandle(),
				signedBy);
		update(meta.getHandle()+":SignedBy",signedBy);
		update(".SignedWith",sKey.getHandle());
	}
	
	public ECDSASignatureBuilder update(String label, String input){
		if(input == null) throw new RuntimeException("Input is null: "+label);
		references.add(label);
		byte [] bytes = input.getBytes(Charset.forName("UTF-8"));
		digest.update(bytes, 0, bytes.length);
		return this;
	}
	
	public ECDSASignatureBuilder update(String label,byte[] bytes){
		if(bytes == null) throw new RuntimeException("Input is null: "+label);
		references.add(label);
		digest.update(bytes, 0, bytes.length);
		return this;
	}
	
	public ECDSACryptoSignature build(){
		
		byte [] bytes = new byte[digest.getDigestSize()];
		digest.doFinal(bytes, 0);
		digest.reset();
		
		ECDSACryptoSignature sig = CryptoFactory.INSTANCE.sign(signedBy, sKey, bytes);
		for(String ref: references) {
			sig.addDataReference(ref);
		}
		references.clear();
		return sig;
	}

}
