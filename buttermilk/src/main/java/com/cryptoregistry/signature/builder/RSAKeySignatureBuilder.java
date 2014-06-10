package com.cryptoregistry.signature.builder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import x.org.bouncycastle.crypto.Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;

import com.cryptoregistry.rsa.CryptoFactory;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.signature.RSACryptoSignature;


/**<pre>
 * 
 * construct a signature using RSA. Process:
 * 
 * builder(registration handle, signer key contents, digest)
 * builder.update("handle:token", bytes0)
 * builder.update("handle:token", bytes1)
 * builder.update("handle:token", bytes2)
 * RSACRyptoSignature sig = builder.build()
 * 
 * </pre>
 * @author Dave
 *
 */
public class RSAKeySignatureBuilder {

	final RSAKeyContents sKey;
	final Digest digest;
	final List<String> references;
	final String signedBy;
	
	public RSAKeySignatureBuilder(String signedBy, RSAKeyContents sKey, Digest digest) {
		super();
		this.sKey = sKey;
		this.digest = digest;
		this.references=new ArrayList<String>();
		this.signedBy = signedBy;
	}
	
	public RSAKeySignatureBuilder(String signedBy, RSAKeyContents sKey) {
		super();
		this.sKey = sKey;
		this.digest = new SHA256Digest();
		this.references=new ArrayList<String>();
		this.signedBy = signedBy;
	}
	
	public RSAKeySignatureBuilder update(String label, String input){
		references.add(label);
		byte [] bytes = input.getBytes(Charset.forName("UTF-8"));
		digest.update(bytes, 0, bytes.length);
		return this;
	}
	
	public RSAKeySignatureBuilder update(String label,byte[] bytes){
		references.add(label);
		digest.update(bytes, 0, bytes.length);
		return this;
	}
	
	public RSACryptoSignature build(){
		byte [] bytes = new byte[digest.getDigestSize()];
		digest.doFinal(bytes, 0);
		digest.reset();
		RSACryptoSignature sig = CryptoFactory.INSTANCE.sign(signedBy, sKey, bytes);
		for(String ref: references) {
			sig.addDataReference(ref);
		}
		references.clear();
		return sig;
	}

}
