/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.ntru;

import com.cryptoregistry.util.ArmoredCompressedString;
import com.cryptoregistry.util.ArrayUtil;

import x.org.bouncycastle.pqc.math.ntru.polynomial.DenseTernaryPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.Polynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.ProductFormPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.SparseTernaryPolynomial;

public class PolynomialAdapter {
	
	// use with h and fp
	public static IntegerPolynomial unwrapIntegerPolynomial(String encoded){
		ArmoredCompressedString str = new ArmoredCompressedString(encoded);
		int [] array = ArrayUtil.unwrapIntArray(str);
		return new IntegerPolynomial(array);
	}

	public static Polynomial unwrapDense(String encoded){
		ArmoredCompressedString str = new ArmoredCompressedString(encoded);
		int [] array = ArrayUtil.unwrapIntArray(str);
		return new DenseTernaryPolynomial(array);
	}
	
	public static Polynomial unwrapSparse(String encoded){
		ArmoredCompressedString str = new ArmoredCompressedString(encoded);
		int [] array = ArrayUtil.unwrapIntArray(str);
		return new SparseTernaryPolynomial(array);
	}
	
	public static Polynomial unwrapProductForm(String encoded0, String encoded1, String encoded2){
		
		ArmoredCompressedString str0 = new ArmoredCompressedString(encoded0);
		int [] array0 = ArrayUtil.unwrapIntArray(str0);
		SparseTernaryPolynomial sp0 = new SparseTernaryPolynomial(array0);
		
		ArmoredCompressedString str1 = new ArmoredCompressedString(encoded1);
		int [] array1 = ArrayUtil.unwrapIntArray(str1);
		SparseTernaryPolynomial sp1 = new SparseTernaryPolynomial(array1);
		
		ArmoredCompressedString str2 = new ArmoredCompressedString(encoded2);
		int [] array2 = ArrayUtil.unwrapIntArray(str2);
		SparseTernaryPolynomial sp2 = new SparseTernaryPolynomial(array2);
		
		return new ProductFormPolynomial(sp0,sp1,sp2);
	}
}
