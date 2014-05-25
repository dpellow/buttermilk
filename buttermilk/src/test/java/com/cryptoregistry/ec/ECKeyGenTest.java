package com.cryptoregistry.ec;

import org.junit.Test;

public class ECKeyGenTest {

	@Test
	public void test0() {
		String curve = "secp112r1";
		CryptoFactory.INSTANCE.generateKeys(curve);
	}

}
