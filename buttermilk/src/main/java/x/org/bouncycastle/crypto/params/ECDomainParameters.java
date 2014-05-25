package x.org.bouncycastle.crypto.params;

import java.math.BigInteger;


import x.org.bouncycastle.math.ec.ECConstants;
import x.org.bouncycastle.math.ec.ECCurve;
import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.util.Arrays;

public class ECDomainParameters implements ECConstants {
	
	private final ECCurve curve;
	private final byte[] seed;
	private final ECPoint G;
	private final BigInteger n;
	private final BigInteger h;
	
	private final String name;

	public ECDomainParameters(ECCurve curve, ECPoint G, BigInteger n) {
		this(curve, G, n, ONE, null,null);
	}

	public ECDomainParameters(ECCurve curve, ECPoint G, BigInteger n,
			BigInteger h) {
		this(curve, G, n, h, null,null);
	}

	public ECDomainParameters(ECCurve curve, ECPoint G, BigInteger n,
			BigInteger h, byte[] seed, String name) {
		this.curve = curve;
		this.G = G.normalize();
		this.n = n;
		this.h = h;
		this.seed = seed;
		this.name = name;
	}

	public ECCurve getCurve() {
		return curve;
	}

	public ECPoint getG() {
		return G;
	}

	public BigInteger getN() {
		return n;
	}

	public BigInteger getH() {
		return h;
	}

	public byte[] getSeed() {
		return Arrays.clone(seed);
	}

	public String getName() {
		return name;
	}

}
