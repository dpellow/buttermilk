package com.cryptoregistry.ec;

import java.math.BigInteger;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

public class Secp128r2 extends ECParametersHolderBase {

	private static final String NAME = "secp118r2";

	private Secp128r2(ECDomainParameters params) {
		super(params);
	}

	static Secp128r2 instance() {
		ECDomainParameters p = init(NAME);
		return new Secp128r2(p);
	}

	private static ECDomainParameters init(String name) {
		 // p = 2^128 - 2^97 - 1
        BigInteger p = fromHex("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFF");
        BigInteger a = fromHex("D6031998D1B3BBFEBF59CC9BBFF9AEE1");
        BigInteger b = fromHex("5EEEFCA380D02919DC2C6558BB6D8A5D");
        byte[] S = Hex.decode("004D696E67687561517512D8F03431FCE63B88F4");
        BigInteger n = fromHex("3FFFFFFF7FFFFFFFBE0024720613B5A3");
        BigInteger h = BigInteger.valueOf(4);

        ECCurve curve = new ECCurve.Fp(p, a, b);
        //ECPoint G = curve.decodePoint(Hex.decode("02"
        //+ "7B6AA5D85E572983E6FB32A7CDEBC140"));
        ECPoint G = curve.decodePoint(Hex.decode("04"
            + "7B6AA5D85E572983E6FB32A7CDEBC140"
            + "27B6916A894D3AEE7106FE805FC34B44"));

		return new ECDomainParameters(curve, G, n, h, S, name);
	}

	@Override
	public ECDomainParameters createParameters() {
		return cached;
	}

}
