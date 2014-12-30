package com.cryptoregistry.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class ArmoredStringTest {

	@Test
	public void test0() throws NoSuchAlgorithmException {
		// compare Armored and ArmoredCompressed for arrays similar to used in NTRU
		Random rand = new Random();
		int [] array = new int[1000];
		for(int i = 0; i<1000;i++){
			if(rand.nextInt()%2 == 0){
				array[i]=0;
			}else{
				array[i]=1;
			}
		}
		ArmoredString as = ArrayUtil.wrapIntArray(array);
		ArmoredCompressedString acs = ArrayUtil.wrapAndCompressIntArray(array);
		
		// compresses by at least a factor of 10, which seems worth the extra hassle
		Assert.assertTrue(as.length()>acs.length()*10);
		System.err.println(as.length());
		System.err.println(acs.length());
	}
	
	@Test
	public void test1(){
		int [] array = {1,2,3,4,5,6,7,8,9,10};
		try {
			byte [] compressed = ArrayUtil.compressIntArray(array);
			int [] out = ArrayUtil.uncompressIntArray(compressed);
			Assert.assertTrue(Arrays.equals(array, out));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
