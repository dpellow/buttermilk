/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.Writer;

import com.cryptoregistry.ntru.NTRUNamedParameters;
import com.cryptoregistry.util.ArmoredString;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;

/**
 * For a named param, use the appropriate constructor. To serialize the attributes, 
 * use the param constructor
 * 
 * @author Dave
 *
 */
public class NTRUParametersFormatter {

	private final NTRUEncryptionParameters params;
	private final NTRUNamedParameters paramEnum;

	public NTRUParametersFormatter(NTRUEncryptionParameters params) {
		this.params = params;
		this.paramEnum = null;
	}

	public NTRUParametersFormatter(NTRUNamedParameters e) {
		this.params = e.getParameters();
		this.paramEnum = e;
	}

	public boolean usesNamedParameter() {
		return this.paramEnum!= null;
	}

	public void format(JsonGenerator g, Writer writer)
			throws JsonGenerationException, IOException {

		if (usesNamedParameter()) {
			g.writeStringField("NamedParameters", this.paramEnum.name());
		} else {
			g.writeObjectFieldStart("NTRUParams");
			g.writeStringField("PolyType", String.valueOf(params.polyType));
			g.writeStringField("N", String.valueOf(params.N));
			g.writeStringField("q", String.valueOf(params.q));
			g.writeStringField("df", String.valueOf(params.df));
			g.writeStringField("df1", String.valueOf(params.df1));
			g.writeStringField("df2", String.valueOf(params.df2));
			g.writeStringField("df3", String.valueOf(params.df3));
			g.writeStringField("db", String.valueOf(params.db));
			g.writeStringField("dm0", String.valueOf(params.dm0));
			g.writeStringField("c", String.valueOf(params.c));
			g.writeStringField("minCallsR", String.valueOf(params.minCallsR));
			g.writeStringField("minCallsMask", String.valueOf(params.minCallsMask));
			g.writeStringField("hashSeed", String.valueOf(params.hashSeed));
			g.writeStringField("oid", new ArmoredString(params.oid).toString());
			g.writeStringField("sparse", String.valueOf(params.sparse));
			g.writeStringField("fastFp", String.valueOf(params.fastFp));
			g.writeStringField("DigestAlgorithm",
					String.valueOf(params.hashAlg.getAlgorithmName()));
			g.writeEndObject();
		}
	}

}
