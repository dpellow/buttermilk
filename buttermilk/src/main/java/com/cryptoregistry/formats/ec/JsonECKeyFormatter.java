/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.ec;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.cryptoregistry.Version;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatKeys;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonECKeyFormatter  implements FormatKeys {

	protected ECKeyContents ecKeys;
	protected PBEParams pbeParams;

	public JsonECKeyFormatter(ECKeyContents ecKeys, PBEParams pbeParams) {
		super();
		this.ecKeys = ecKeys;
		this.pbeParams = pbeParams;
	}

	public void formatKeys(Mode mode, Encoding enc, Writer writer) {

		switch (mode) {
		case OPEN: {
			formatOpen(enc, writer);
			break;
		}
		case SEALED: {
			seal(enc, writer);
			break;
		}
		case FOR_PUBLICATION: {
			formatForPublication(enc, writer);
			break;
		}
		default:
			throw new RuntimeException("Unknown mode");
		}

	}

	protected void seal(Encoding enc, Writer writer) {

		String plain = formatItem(enc, ecKeys);
		ArmoredPBEResult result;
		try {
			byte[] plainBytes = plain.getBytes("UTF-8");
			PBE pbe0 = new PBE(pbeParams);
			result = pbe0.encrypt(plainBytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();

			g.writeStartObject();
			g.writeStringField("Version", Version.VERSION);
			g.writeStringField("CreatedOn", TimeUtil.now());
				g.writeObjectFieldStart("Keys");
					g.writeObjectFieldStart(ecKeys.handle);
					g.writeStringField("KeyData.Type", "EC");
					g.writeStringField("KeyData.PBEAlgorithm", pbeParams.getAlg().toString());
					g.writeStringField("KeyData.EncryptedData", result.base64Enc);
					g.writeStringField("KeyData.PBESalt", result.base64Salt);
					
					if (result instanceof ArmoredPBKDF2Result) {
						// specific to PBKDF2
						g.writeStringField("KeyData.Iterations", String.valueOf(((ArmoredPBKDF2Result) result).iterations));

					} else if (result instanceof ArmoredScryptResult) {
						// specific to Scrypt
						g.writeStringField("KeyData.IV",((ArmoredScryptResult) result).base64IV);
						g.writeStringField("KeyData.BlockSize", String.valueOf(((ArmoredScryptResult) result).blockSize));
						g.writeStringField(
								"KeyData.CpuMemoryCost", 
								String.valueOf(((ArmoredScryptResult) result).cpuMemoryCost));
						g.writeStringField(
								"KeyData.Parallelization",
								String.valueOf(((ArmoredScryptResult) result).parallelization));
					}
				g.writeEndObject();
			g.writeEndObject();
		} catch (IOException x) {
			throw new RuntimeException(x);
		} finally {
			try {
				if (g != null)
					g.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void formatOpen(Encoding enc, Writer writer) {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();

			g.writeStartObject();
				g.writeStringField("Version", Version.VERSION);
				g.writeStringField("CreatedOn", TimeUtil.now());
					g.writeObjectFieldStart("Keys");
						g.writeObjectFieldStart(ecKeys.handle);
							g.writeStringField("Encoding", enc.toString());
							g.writeStringField("Q", FormatUtil.serializeECPoint(ecKeys.Q, enc));
							g.writeStringField("D", FormatUtil.wrap(enc, ecKeys.d));
							g.writeStringField("CurveName", ecKeys.curveName);
					g.writeEndObject();
				g.writeEndObject();
		} catch (IOException e) {
			//throw new RuntimeException(e);
			e.printStackTrace();
		} finally {
			try {
				if (g != null)
					g.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void formatForPublication(Encoding enc, Writer writer) {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();

			g.writeStartObject();
			g.writeStringField("Version", Version.VERSION);
			g.writeStringField("CreatedOn", TimeUtil.now());
				g.writeObjectFieldStart("Keys");
					g.writeObjectFieldStart(ecKeys.handle);
						g.writeStringField("Encoding", enc.toString());
						g.writeStringField("Q", FormatUtil.serializeECPoint(ecKeys.Q, enc));
						g.writeStringField("CurveName", ecKeys.curveName);
						g.writeEndObject();
						g.writeEndObject();
					g.writeEndObject();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (g != null)
						g.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
	}

	private String formatItem(Encoding enc, ECKeyContents item) {
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeStringField("Encoding", enc.toString());
			g.writeStringField("Q", FormatUtil.serializeECPoint(ecKeys.Q, enc));
			g.writeStringField("D", FormatUtil.wrap(enc, ecKeys.d));
			g.writeStringField("CurveName", ecKeys.curveName);
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
