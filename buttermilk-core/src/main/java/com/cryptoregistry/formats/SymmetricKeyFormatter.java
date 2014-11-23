/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class SymmetricKeyFormatter {

	protected final SymmetricKeyContents sKey;
	protected final KeyFormat format;
	protected final PBEParams pbeParams;

	public SymmetricKeyFormatter(SymmetricKeyContents sKey) {
		super();
		this.sKey = sKey;
		this.format = sKey.metadata.format;
		this.pbeParams = sKey.metadata.format.pbeParams;
	}

	public void formatKeys(JsonGenerator g, Writer writer) {

		try {
			switch (format.mode) {
			case UNSECURED: {
				formatOpen(g, format.encodingHint, writer);
				break;
			}
			case SECURED: {
				seal(g, format.encodingHint, writer);
				break;
			}
			case FOR_PUBLICATION: {
				throw new RuntimeException("For_Publication mode not available for this key type");
			}
			default:
				throw new RuntimeException("Unknown mode");
			}
		}catch(Exception x){
			throw new RuntimeException(x);
		}
		
	}

	protected void seal(JsonGenerator g, EncodingHint enc, Writer writer)
			throws JsonGenerationException, IOException {

		String plain = formatItem(enc, (SymmetricKeyContents)sKey);
		ArmoredPBEResult result;
		try {
			byte[] plainBytes = plain.getBytes("UTF-8");
			PBE pbe0 = new PBE(pbeParams);
			result = pbe0.encrypt(plainBytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		g.writeObjectFieldStart(sKey.metadata.getDistinguishedHandle());
		g.writeStringField("KeyData.Type", "Symmetric");
		g.writeStringField("KeyData.PBEAlgorithm", pbeParams.getAlg()
				.toString());
		g.writeStringField("KeyData.EncryptedData", result.base64Enc);
		g.writeStringField("KeyData.PBESalt", result.base64Salt);

		if (result instanceof ArmoredPBKDF2Result) {
			// specific to PBKDF2
			g.writeStringField("KeyData.Iterations",
					String.valueOf(((ArmoredPBKDF2Result) result).iterations));

		} else if (result instanceof ArmoredScryptResult) {
			// specific to Scrypt
			g.writeStringField("KeyData.IV",
					((ArmoredScryptResult) result).base64IV);
			g.writeStringField("KeyData.BlockSize",
					String.valueOf(((ArmoredScryptResult) result).blockSize));
			g.writeStringField("KeyData.CpuMemoryCost", String
					.valueOf(((ArmoredScryptResult) result).cpuMemoryCost));
			g.writeStringField("KeyData.Parallelization", String
					.valueOf(((ArmoredScryptResult) result).parallelization));
		}
		g.writeEndObject();

	}

	protected void formatOpen(JsonGenerator g, EncodingHint enc, Writer writer)
			throws JsonGenerationException, IOException {

		g.writeObjectFieldStart(sKey.metadata.getDistinguishedHandle());
		g.writeStringField("KeyAlgorithm", "Symmetric");
		g.writeStringField("CreatedOn", TimeUtil.format(sKey.metadata.createdOn));
		g.writeStringField("Encoding", EncodingHint.Base64url.toString());
		g.writeStringField("s", sKey.getBase64UrlEncoding());
		g.writeEndObject();
	}

	private String formatItem(EncodingHint enc, SymmetricKeyContents item) {
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeObjectFieldStart(sKey.metadata.getHandle()+"-U");
			g.writeStringField("KeyAlgorithm", "Symmetric");
			g.writeStringField("CreatedOn", TimeUtil.format(sKey.metadata.createdOn));
			g.writeStringField("Encoding", EncodingHint.Base64url.toString());
			g.writeStringField("s", sKey.getBase64UrlEncoding());
			g.writeEndObject();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				g.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return privateDataWriter.toString();
	}

}
