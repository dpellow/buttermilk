/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */

package com.cryptoregistry.c2;

import java.nio.charset.Charset;

import org.junit.Test;

import x.org.bouncycastle.crypto.digests.SHA256Digest;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.c2.key.SecretKey;
import com.cryptoregistry.c2.key.SigningPrivateKey;
import com.cryptoregistry.signature.C2Signature;
import com.cryptoregistry.util.XORUtil;

import junit.framework.Assert;

public class Curve25519Test {

	@Test
	public void test0() {
		Curve25519KeyContents keys0 = CryptoFactory.INSTANCE.generateKeys();
		Curve25519KeyContents keys1 = CryptoFactory.INSTANCE.generateKeys();
		SecretKey s0 = CryptoFactory.INSTANCE.keyAgreement(keys1.publicKey, keys0.agreementPrivateKey);
		SecretKey s1 = CryptoFactory.INSTANCE.keyAgreement(keys0.publicKey, keys1.agreementPrivateKey);
		Assert.assertTrue(test_equal(s0.getBytes(),s1.getBytes()));
		System.err.println(s0.getBase64Encoding());
		System.err.println(s1.getBase64Encoding());
	}
	
	private boolean test_equal(byte[] a, byte[] b) {
		int i;
		for (i = 0; i < 32; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}
	
	
	/* deterministic EC-KCDSA
	 *
	 *    s is the private key for signing
	 *    P is the corresponding public key
	 *    Z is the context data (signer public key or certificate, etc)
	 *
	 * signing:
	 *
	 *    m = hash(Z, message)
	 *    x = hash(m, s)
	 *    keygen25519(Y, NULL, x);
	 *    r = hash(Y);
	 *    h = m XOR r
	 *    sign25519(v, h, x, s);
	 *
	 *    output (v,r) as the signature
	 *
	 * verification:
	 *
	 *    m = hash(Z, message);
	 *    h = m XOR r
	 *    verify25519(Y, v, h, P)
	 *
	 *    confirm  r == hash(Y)
	 *
	 * It would seem to me that it would be simpler to have the signer directly do 
	 * h = hash(m, Y) and send that to the recipient instead of r, who can verify 
	 * the signature by checking h == hash(m, Y).  If there are any problems with 
	 * such a scheme, please let me know.
	 *
	 * Also, EC-KCDSA (like most DS algorithms) picks x random, which is a waste of 
	 * perfectly good entropy, but does allow Y to be calculated in advance of (or 
	 * parallel to) hashing the message.
	 */
	
	/* Signature generation primitive, calculates (x-h)s mod q
	 *   v  [out] signature value
	 *   h  [in]  signature hash (of message, signature pub key, and context data)
	 *   x  [in]  signature private key
	 *   s  [in]  private key for signing
	 * returns true on success, false on failure (use different x or h)
	 */
	
	/* Signature verification primitive, calculates Y = vP + hG
	 *   Y  [out] signature public key
	 *   v  [in]  signature value
	 *   h  [in]  signature hash
	 *   P  [in]  public key
	 */
	
	@Test
	public void test1() {
		Curve25519 c2 = new Curve25519();
		
		String message = "My message text...";
		byte[]msgBytes = message.getBytes(Charset.forName("UTF-8"));
		
		Curve25519KeyContents c2Keys = CryptoFactory.INSTANCE.generateKeys();
		SigningPrivateKey spk = c2Keys.signingPrivateKey;
		PublicKey pKey = c2Keys.publicKey;
		
		// compute m
		SHA256Digest digest = new SHA256Digest();
		digest.update(pKey.getBytes(), 0, pKey.length());
		digest.update(msgBytes, 0, msgBytes.length);
		byte [] m = new byte[32];
		digest.doFinal(m, 0);
		
		// compute x
		digest = new SHA256Digest();
		digest.update(m, 0, m.length);
		digest.update(spk.getBytes(), 0, spk.length());
		byte [] x = new byte[32];
		digest.doFinal(x, 0);
		
		// compute Y
		// keygen25519(Y, NULL, x);
		byte [] Y = new byte[32];
		c2.keygen(Y, null, x);
		
		// compute r
		byte [] r = new byte[32];
		digest.update(Y,0,Y.length);
		digest.doFinal(r, 0);
		
		// compute h: m XOR r
		byte [] h = XORUtil.xor(m, r);
		
		// sign, v is the signature
		byte [] v = new byte[32];
		boolean ok = c2.sign(v, h, x, spk.getBytes());
		Assert.assertTrue(ok);
		
		// compute Y again
		byte [] Yx = new byte[32];
		c2.verify(Yx, v, h, pKey.getBytes());
		
		// get hash(Yx)
		digest.update(Yx, 0, Yx.length);
		byte [] check = new byte[32];
		digest.doFinal(check, 0);
		
		Assert.assertTrue(test_equal(r,check));
	}
	
	@Test
	public void test2() {
		
		String message = "My message text...";
		byte[]msgBytes = message.getBytes(Charset.forName("UTF-8"));
		
		Curve25519KeyContents c2Keys = CryptoFactory.INSTANCE.generateKeys();
		C2Signature sig = CryptoFactory.INSTANCE.sign(c2Keys, msgBytes);
		boolean ok = CryptoFactory.INSTANCE.verify(c2Keys, msgBytes, sig);
		Assert.assertTrue(ok);
	}
	
}
