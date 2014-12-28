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
	
	public ECDomainParameters(ECCurve curve, ECPoint G, BigInteger n, String name) {
		this(curve, G, n, ONE, null,name);
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
	
	public ECDomainParameters alias(String alias){
		return new ECDomainParameters(curve,G,n,h,seed,alias);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((G == null) ? 0 : G.hashCode());
		result = prime * result + ((curve == null) ? 0 : curve.hashCode());
		result = prime * result + ((h == null) ? 0 : h.hashCode());
		result = prime * result + ((n == null) ? 0 : n.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + java.util.Arrays.hashCode(seed);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ECDomainParameters other = (ECDomainParameters) obj;
		if (G == null) {
			if (other.G != null)
				return false;
		} else if (!G.equals(other.G))
			return false;
		if (curve == null) {
			if (other.curve != null)
				return false;
		} else if (!curve.equals(other.curve))
			return false;
		if (h == null) {
			if (other.h != null)
				return false;
		} else if (!h.equals(other.h))
			return false;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!java.util.Arrays.equals(seed, other.seed))
			return false;
		return true;
	}
	
	

}
