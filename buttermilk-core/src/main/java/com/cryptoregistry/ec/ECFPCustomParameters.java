package com.cryptoregistry.ec;

import java.math.BigInteger;
import java.util.UUID;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.util.ArmoredString;

import x.org.bouncycastle.crypto.params.ECDomainParameters;
import x.org.bouncycastle.math.ec.ECPoint;

public class ECFPCustomParameters extends ECCustomParameters {

	private ECFPCustomParameters() {
		super(FIELD.FP, UUID.randomUUID().toString());
		
	}
	
	/**
	 * uuid is user assigned. Encoding cannot be null
	 * 
	 * @param uuid
	 * @param enc
	 * @param p
	 * @param a
	 * @param b
	 * @param seed
	 * @param n
	 * @param h
	 * @param G
	 */
	public ECFPCustomParameters(String uuid, Encoding enc, BigInteger p, BigInteger a, BigInteger b, byte [] seed, BigInteger n, BigInteger h, ECPoint G){
		super(FIELD.FP,uuid);
		String _p = FormatUtil.wrap(enc, p);
		String _a = FormatUtil.wrap(enc, a);
		String _b = FormatUtil.wrap(enc, b);
		ArmoredString _seed = new ArmoredString(seed);
		String _n = FormatUtil.wrap(enc, n);
		String _h = FormatUtil.wrap(enc, h);
		String _G = FormatUtil.serializeECPoint(G, enc);
		this.parameters.put("Encoding", enc.toString());
		this.parameters.put("p", _p);
		this.parameters.put("a", _a);
		this.parameters.put("b", _b);
		this.parameters.put("S", _seed.toString());
		this.parameters.put("n", _n);
		this.parameters.put("h", _h);
		this.parameters.put("G", _G);
	}
	
	/**
	 * Default to Base 16 encoding on the big integers; uuid is randomly constructed
	 * 
	 * @param p
	 * @param a
	 * @param b
	 * @param seed
	 * @param n
	 * @param h
	 * @param G
	 */
	public ECFPCustomParameters(BigInteger p, BigInteger a, BigInteger b, byte [] seed, BigInteger n, BigInteger h, ECPoint G){
		this();
		Encoding enc = Encoding.Base16;
		String _p = FormatUtil.wrap(enc, p);
		String _a = FormatUtil.wrap(enc, a);
		String _b = FormatUtil.wrap(enc, b);
		ArmoredString _seed = new ArmoredString(seed);
		String _n = FormatUtil.wrap(enc, n);
		String _h = FormatUtil.wrap(enc, h);
		String _G = FormatUtil.serializeECPoint(G, enc);
		this.parameters.put("Encoding", enc.toString());
		this.parameters.put("p", _p);
		this.parameters.put("a", _a);
		this.parameters.put("b", _b);
		this.parameters.put("S", _seed.toString());
		this.parameters.put("n", _n);
		this.parameters.put("h", _h);
		this.parameters.put("G", _G);
	}

	@Override
	public ECDomainParameters getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}
