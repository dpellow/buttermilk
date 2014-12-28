package x.org.bouncycastle.crypto.params;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.encoders.Hex;

import com.cryptoregistry.ec.CurveFactory;

/**
 * Test equals methods added to BC's code
 * 
 * @author Dave
 *
 */
public class ECDomainParametersTest {

	@Test
	public void test0() {
		ECDomainParameters params0 = CurveFactory.getCurveForName("P-256");
		ECDomainParameters params1 = CurveFactory.getCurveForName("P-256");
		// same object, so should work
		Assert.assertEquals(params0, params1);
		
		// p = 2^224 (2^32 - 1) + 2^192 + 2^96 - 1
		BigInteger p = fromHex("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF");
		BigInteger a = fromHex("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC");
		BigInteger b = fromHex("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B");
		byte[] S = Hex.decode("C49D360886E704936A6678E1139D26B7819F7E90");
		BigInteger n = fromHex("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551");
		BigInteger h = BigInteger.valueOf(1);

		ECCurve curve0 = new ECCurve.Fp(p, a, b);
		ECPoint G0 = curve0
				.decodePoint(Hex
						.decode("04"
								+ "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296"
								+ "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5"));
		
		ECCurve curve1 = new ECCurve.Fp(p, a, b);
		ECPoint G1 = curve1
				.decodePoint(Hex
						.decode("04"
								+ "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296"
								+ "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5"));

		ECDomainParameters p0 = new ECDomainParameters(curve0, G0, n, h, S, "P256");
		ECDomainParameters p1 = new ECDomainParameters(curve1, G1, n, h, S, "P256");
		// test deeper equality
		Assert.assertEquals(p0, p1);
	}
	
	protected static BigInteger fromHex(String hex) {
		return new BigInteger(1, Hex.decode(hex));
	}

}
