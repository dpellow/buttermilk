package com.cryptography.ntru;

import java.io.StringWriter;
import java.nio.charset.Charset;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.pqc.math.ntru.polynomial.DenseTernaryPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.ProductFormPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.SparseTernaryPolynomial;
import x.org.bouncycastle.util.Arrays;

import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.ntru.CryptoFactory;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUNamedParameters;
import com.cryptoregistry.util.ArmoredCompressedString;
import com.cryptoregistry.util.ArrayUtil;

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
		NTRUKeyContents dup_skey = null;
		
		for(NTRUNamedParameters p: NTRUNamedParameters.values()){
			System.out.println(p.toString());
			NTRUKeyContents sKey = CryptoFactory.INSTANCE.generateKeys(p.getKeyGenerationParameters());
			
			final String registrationHandle = "Chinese Eyes";
			JSONFormatter f = new JSONFormatter(registrationHandle);
			f.add(sKey);
			StringWriter writer = new StringWriter();
			f.format(writer);
			System.err.println(writer.toString());
			
			ArmoredCompressedString _h = sKey.wrappedH();
			IntegerPolynomial h = new IntegerPolynomial(ArrayUtil.unwrapIntArray(_h));
			ArmoredCompressedString _fp = sKey.wrappedFp();
			IntegerPolynomial fp = new IntegerPolynomial(ArrayUtil.unwrapIntArray(_fp));
			Object obj = sKey.wrappedT();
			if(obj.getClass().isArray()){
				// product form
				ArmoredCompressedString [] array = (ArmoredCompressedString[])obj;
				SparseTernaryPolynomial t0 = new SparseTernaryPolynomial(ArrayUtil.unwrapIntArray(array[0]));
				SparseTernaryPolynomial t1 = new SparseTernaryPolynomial(ArrayUtil.unwrapIntArray(array[1]));
				SparseTernaryPolynomial t2 = new SparseTernaryPolynomial(ArrayUtil.unwrapIntArray(array[2]));
				ProductFormPolynomial t = new ProductFormPolynomial(t0,t1,t2);
				dup_skey = new NTRUKeyContents(sKey.metadata,sKey.params,h,t,fp);
			}else{
				if(sKey.params.sparse){
					// sparse ternary
					ArmoredCompressedString acs = (ArmoredCompressedString)obj;
					SparseTernaryPolynomial t = new SparseTernaryPolynomial(ArrayUtil.unwrapIntArray(acs));
					dup_skey = new NTRUKeyContents(sKey.metadata,sKey.params,h,t,fp);
				}else{
					// dense ternary
					ArmoredCompressedString acs = (ArmoredCompressedString)obj;
					DenseTernaryPolynomial t = new DenseTernaryPolynomial(ArrayUtil.unwrapIntArray(acs));
					dup_skey = new NTRUKeyContents(sKey.metadata,sKey.params,h,t,fp);
				}
			}
			
			byte [] encrypted = CryptoFactory.INSTANCE.encrypt(dup_skey, in);
			byte [] out = CryptoFactory.INSTANCE.decrypt(dup_skey, encrypted);
			Assert.assertTrue(Arrays.areEqual(in, out));
			
		}
	}

}
