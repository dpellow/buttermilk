/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.util.Map;

import com.cryptoregistry.util.ArmoredString;

import x.org.bouncycastle.crypto.Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.crypto.digests.SHA512Digest;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;


/**
 * Parse what NTRUParametersFormatter can write
 * 
 * @author Dave
 *
 */
public class NTRUParametersReader {

	private final Map<String,Object> input;
	
	public NTRUParametersReader(Map<String,Object> input) {
		this.input = input;
	}
	
	public NTRUEncryptionParameters parse() {
		
	//	int PolyType = Integer.parseInt(String.valueOf(input.get("PolyType")));
		int N = Integer.parseInt(String.valueOf(input.get("N")));
		int q = Integer.parseInt(String.valueOf(input.get("q")));
		int df = Integer.parseInt(String.valueOf(input.get("df")));
		int df1 = Integer.parseInt(String.valueOf(input.get("df1")));
		int df2 = Integer.parseInt(String.valueOf(input.get("df2")));
		int df3 = Integer.parseInt(String.valueOf(input.get("df3")));
		int db = Integer.parseInt(String.valueOf(input.get("db")));
		int dm0 = Integer.parseInt(String.valueOf(input.get("dm0")));
		int c = Integer.parseInt(String.valueOf(input.get("c")));
		int minCallsR = Integer.parseInt(String.valueOf(input.get("minCallsR")));
		int minCallsMask = Integer.parseInt(String.valueOf(input.get("minCallsMask")));
		boolean hashSeed = Boolean.parseBoolean(String.valueOf(input.get("hashSeed")));
		byte [] oid = new ArmoredString(String.valueOf(input.get("oid"))).decodeToBytes();
		boolean sparse = Boolean.parseBoolean(String.valueOf(input.get("sparse")));
		boolean fastFp = Boolean.parseBoolean(String.valueOf(input.get("fastFp")));
		String digestAlg = String.valueOf(input.get("DigestAlgorithm"));
		Digest digest = null;
		if(digestAlg.equals("SHA-256")){
			digest = new SHA256Digest();
		}else{
			digest = new SHA512Digest();
		}
		
		// works because the likelihood of ones appearing the polynomials is rather small
		if(df1 == 0 && df2==0 && df3== 0){
			return new NTRUEncryptionParameters(N, q, df, dm0, db, c, minCallsR, minCallsMask, hashSeed, oid, sparse, fastFp, digest);
		}else{
			return new NTRUEncryptionParameters(N, q, df1, df2, df3, dm0, db, c, minCallsR, minCallsMask, hashSeed, oid, sparse, fastFp, digest);
		}
	}

}
