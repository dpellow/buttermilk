/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ec;

import org.junit.Test;

public class ECCryptoTest {

	@Test
	public void test0() {
		String curveName = "secp112r1";
		ECKeyContents ecc = CryptoFactory.INSTANCE.generateKeys(curveName);
		
	}

}
