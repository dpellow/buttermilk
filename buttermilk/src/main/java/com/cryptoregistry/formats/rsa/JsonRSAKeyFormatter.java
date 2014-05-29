/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.rsa;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;

import net.iharder.Base64;

import com.cryptoregistry.Version;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.util.TimeUtil;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatKeys;
import com.cryptoregistry.formats.Mode;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonRSAKeyFormatter  implements FormatKeys {

	protected RSAKeyContents rsaKeys;
	protected PBEParams pbeParams;

	public JsonRSAKeyFormatter(RSAKeyContents rsaKeys, PBEParams pbeParams) {
		super();
		this.rsaKeys = rsaKeys;
		this.pbeParams = pbeParams;
	}

	public void formatKeys(Mode mode, Encoding enc, Writer writer) {
		switch (mode) {
		case OPEN: {
			formatExposed(enc, writer);
			break;
		}
		case SEALED: {
			formatSealed(enc, writer);
			break;
		}
		case FOR_PUBLICATION: {
			formatKeyExchange(enc, writer);
			break;
		}
		default:
			throw new RuntimeException("Unknown mode");
		}
	}

	protected void formatSealed(Encoding enc, Writer writer) {

		String plain = formatItem(enc, rsaKeys);
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
					g.writeObjectFieldStart(rsaKeys.handle);
					g.writeStringField("KeyData.Type", "RSA");
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

	protected void formatExposed(Encoding enc, Writer writer) {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();

			g.writeStartObject();
				g.writeStringField("Version", Version.VERSION);
				g.writeStringField("CreatedOn", TimeUtil.now());
					g.writeObjectFieldStart("Keys");
						g.writeObjectFieldStart(rsaKeys.handle);
							g.writeStringField("Encoding", enc.toString());
							g.writeStringField("Modulus", wrap(enc, rsaKeys.modulus));
							g.writeStringField("PublicExponent", wrap(enc, rsaKeys.publicExponent));
							g.writeStringField("PrivateExponent", wrap(enc, rsaKeys.privateExponent));
							g.writeStringField("P", wrap(enc, rsaKeys.p));
							g.writeStringField("Q", wrap(enc, rsaKeys.q));
							g.writeStringField("dP", wrap(enc, rsaKeys.dP));
							g.writeStringField("dQ", wrap(enc, rsaKeys.dQ));
							g.writeStringField("qInv", wrap(enc, rsaKeys.qInv));
				//		g.writeEndObject();
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

	protected void formatKeyExchange(Encoding enc, Writer writer) {
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();

			g.writeStartObject();
			g.writeStringField("Version", Version.VERSION);
			g.writeStringField("CreatedOn", TimeUtil.now());
				g.writeObjectFieldStart("Keys");
					g.writeObjectFieldStart(rsaKeys.handle);
						g.writeStringField("Encoding", enc.toString());
						g.writeStringField("Modulus", wrap(enc, rsaKeys.modulus));
						g.writeStringField("PublicExponent", wrap(enc, rsaKeys.publicExponent));
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

	private String wrap(Encoding enc, BigInteger bi) {
		switch (enc) {
		case Base2:
			return bi.toString(2);
		case Base10:
			return bi.toString(10);
		case Base16:
			return bi.toString(16);
		case Base64: {
			return Base64.encodeBytes(bi.toByteArray());
		}
		case Base64url: {
			try {
				return Base64.encodeBytes(bi.toByteArray(), Base64.URL_SAFE);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		default:
			throw new RuntimeException("Unknown encoding: " + enc);
		}
	}

	private String formatItem(Encoding enc, RSAKeyContents item) {
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeStringField("Handle", rsaKeys.handle);
			g.writeStringField("Encoding", enc.toString());
			g.writeStringField("Modulus", wrap(enc, rsaKeys.modulus));
			g.writeStringField("PublicExponent", wrap(enc, rsaKeys.publicExponent));
			g.writeStringField("PrivateExponent", wrap(enc, rsaKeys.privateExponent));
			g.writeStringField("P", wrap(enc, rsaKeys.p));
			g.writeStringField("Q", wrap(enc, rsaKeys.q));
			g.writeStringField("dP", wrap(enc, rsaKeys.dP));
			g.writeStringField("dQ", wrap(enc, rsaKeys.dQ));
			g.writeStringField("qInv", wrap(enc, rsaKeys.qInv));
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
