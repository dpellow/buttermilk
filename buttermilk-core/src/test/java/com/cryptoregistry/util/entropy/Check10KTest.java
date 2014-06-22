package com.cryptoregistry.util.entropy;

import java.security.SecureRandom;

import org.junit.Test;

import com.cryptoregistry.util.Check10KIterator;
import com.cryptoregistry.util.entropy.ShannonEntropy.Result;

public class Check10KTest {
	
	@Test
	public void test1() {
		SecureRandom rand = new SecureRandom();
		byte [] key = new byte[32];
		rand.nextBytes(key);
		Result res = ShannonEntropy.shannonEntropy(new String(key));
		TresBiEntropy bi = new TresBiEntropy(key);
		int croll = (int) bi.calc().bitsOfEntropy;
		int shannon = res.bitsOfEntropy;
		System.err.println(shannon);
		System.err.println(croll);
	}

	@Test
	public void test0() {
		Check10KIterator iter = new Check10KIterator();
		int totalCount = 0;
		int totalEntropyShannon = 0;
		int totalEntropyCroll=0;
		
		int maxBitsShannon = 0;
		String maxPassShannon = null;
		String maxPassCroll = null;
		int maxBitsCroll = 0;
		while(iter.hasNext()){
			String s = iter.next();
			Result res = ShannonEntropy.shannonEntropy(s);
			TresBiEntropy bi = new TresBiEntropy(s.getBytes());
			int croll = (int) bi.calc().bitsOfEntropy;
			int shannon = res.bitsOfEntropy;
			if(maxBitsShannon < shannon) {
				maxBitsShannon = shannon;
				maxPassShannon = s;
			}
			if(maxBitsCroll < croll) {
				maxBitsCroll = croll;
				maxPassCroll = s;
			}
			totalCount++;
			totalEntropyShannon +=shannon;
			totalEntropyCroll +=croll;
		}
		
		System.err.println("Shannon max: "+maxBitsShannon+", "+maxPassShannon);
		System.err.println("Croll max: "+maxBitsCroll+", "+maxPassCroll);
		System.err.println("Avg Bits of entropy, Shannon: "+totalEntropyShannon/totalCount);
		System.err.println("Avg Bits of entropy, Croll: "+totalEntropyCroll/totalCount);
	}

}
