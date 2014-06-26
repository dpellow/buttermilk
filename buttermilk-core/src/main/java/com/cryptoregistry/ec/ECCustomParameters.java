package com.cryptoregistry.ec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import x.org.bouncycastle.crypto.params.ECDomainParameters;

import com.cryptoregistry.ECCustomCurve;
import com.cryptoregistry.util.MapIterator;

/**<pre>
 * Buttermilk would like to provide a defined format for Elliptic Curve parameters so that
 * researchers can easily communicate and use such parameters. 
 * 
 * The author believes it probably always best to use named curves for business or professional use,
 * due to the probability that custom created curves will go wrong, be inefficient, or otherwise fail.
 * 
 * That said, there is no simple format to communicate curve parameters and we'd like to provide that
 * as a demonstration of the robustness of the buttermilk formats.
 * 
 * See http://www.secg.org/collateral/sec2_final.pdf for the basic definitions of how to encode
 * Elliptic Curve parameters. In brief, the prime field parameters are a sextuplet of the form:
 * 
 * T = ( p, a, b, G, n, h)
 * 
 * where p, a and b specify the curve and G is a base point on the curve, and n is a prime 
 * which is of the order G, and h is the cofactor.
 * 
 * For parameters over the binary field F2m, use the septuple:
 * 
 * T = (m, f(x), a, b, G, n, h)
 * 
 * where m is an integer, f(x) is a polynomial, G is a base point on the curve, and n is a prime 
 * which is of the order G, and h is the cofactor. However, for purposes of representation, we will
 * support f(x) either as Trinomial Polynomial Basis (TPB) or Pentanomial Polynomial Basis (PPB).
 * The target BouncyCastle curve constructors look like this
 * 
         * Constructor for Trinomial Polynomial Basis (TPB).
         * m  The exponent <code>m</code> of
         * <code>F<sub>2<sup>m</sup></sub></code>.
         * k The integer <code>k</code> where <code>x<sup>m</sup> +
         * x<sup>k</sup> + 1</code> represents the reduction
         * polynomial <code>f(z)</code>.
         * a The coefficient <code>a</code> in the Weierstrass equation
         * for non-supersingular elliptic curves over
         * <code>F<sub>2<sup>m</sup></sub></code>.
         * b The coefficient <code>b</code> in the Weierstrass equation
         * for non-supersingular elliptic curves over
         * <code>F<sub>2<sup>m</sup></sub></code>.
         * n The order of the main subgroup of the elliptic curve.
         * h The cofactor of the elliptic curve, i.e.
         * <code>#E<sub>a</sub>(F<sub>2<sup>m</sup></sub>) = h * n</code>.
         * 
          * Constructor for Pentanomial Polynomial Basis (PPB).
         * m  The exponent <code>m</code> of
         * <code>F<sub>2<sup>m</sup></sub></code>.
         * k1 The integer <code>k1</code> where <code>x<sup>m</sup> +
         * x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
         * represents the reduction polynomial <code>f(z)</code>.
         * k2 The integer <code>k2</code> where <code>x<sup>m</sup> +
         * x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
         * represents the reduction polynomial <code>f(z)</code>.
         * k3 The integer <code>k3</code> where <code>x<sup>m</sup> +
         * x<sup>k3</sup> + x<sup>k2</sup> + x<sup>k1</sup> + 1</code>
         * represents the reduction polynomial <code>f(z)</code>.
         * a The coefficient <code>a</code> in the Weierstrass equation
         * for non-supersingular elliptic curves over
         * <code>F<sub>2<sup>m</sup></sub></code>.
         * b The coefficient <code>b</code> in the Weierstrass equation
         * for non-supersingular elliptic curves over
         * <code>F<sub>2<sup>m</sup></sub></code>.
         * n The order of the main subgroup of the elliptic curve.
         * h The cofactor of the elliptic curve, i.e.
         * <code>#E<sub>a</sub>(F<sub>2<sup>m</sup></sub>) = h * n</code>.
         
         
 * 
 * It is beyond the author's present ability to find or define new useful curves; however, just
 * to provide a way to encode them, given user-provided values of the above forms, is not too difficult. 
 * 
 * </pre>
 * 
 * @author Dave
 *
 */
public abstract class ECCustomParameters implements MapIterator, ECCustomCurve {
	
	protected final String uuid;
	protected final Map<String,String> parameters;
	protected final FIELD field;
	
	protected ECCustomParameters(FIELD field) {
		uuid = UUID.randomUUID().toString();
		parameters = new LinkedHashMap<String,String>();
		this.field = field;
	}
	
	protected ECCustomParameters(FIELD field, String uuid) {
		this.uuid = uuid;
		parameters = new LinkedHashMap<String,String>();
		this.field = field;
	}
	
	@Override
	public abstract ECDomainParameters getParameters();
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String next() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getHandle() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static enum FIELD {
		FP, F2M;
	}

}
