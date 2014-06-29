package com.cryptography.ntru;

import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.pqc.crypto.ntru.NTRUParameters;
import x.org.bouncycastle.pqc.math.ntru.polynomial.DenseTernaryPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.Polynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.ProductFormPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.SparseTernaryPolynomial;
import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.ntru.CryptoFactory;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUNamedParameters;
import com.cryptoregistry.util.ArmoredString;

public class CryptoFactoryTest {

	@Test
	public void test0() {
		
		byte [] in = "Hello NTRU world".getBytes(Charset.forName("UTF-8"));
		NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys();
		
		byte [] encrypted = CryptoFactory.INSTANCE.encrypt(sKey, in);
		byte [] out = CryptoFactory.INSTANCE.decrypt(sKey, encrypted);
		Assert.assertTrue(Arrays.areEqual(in, out));
	}
	
	@Test
	public void test1() {
		
		byte [] in = "Hello NTRU world".getBytes(Charset.forName("UTF-8"));
		for(NTRUNamedParameters p: NTRUNamedParameters.values()){
			System.out.println(p.toString());
			NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys(p.getKeyGenerationParameters());
			byte [] encrypted = CryptoFactory.INSTANCE.encrypt(sKey, in);
			byte [] out = CryptoFactory.INSTANCE.decrypt(sKey, encrypted);
			Assert.assertTrue(Arrays.areEqual(in, out));
		}
	}
	
	@Test
	public void test2() {
		
		byte [] in = "Hello NTRU world".getBytes(Charset.forName("UTF-8"));
		for(NTRUNamedParameters p: NTRUNamedParameters.values()){
			System.out.println(p.toString());
			NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys(p.getKeyGenerationParameters());
			System.out.println(sKey.toString());
			
			
			byte [] encrypted = CryptoFactory.INSTANCE.encrypt(sKey, in);
			byte [] out = CryptoFactory.INSTANCE.decrypt(sKey, encrypted);
			Assert.assertTrue(Arrays.areEqual(in, out));
		}
	}

}
