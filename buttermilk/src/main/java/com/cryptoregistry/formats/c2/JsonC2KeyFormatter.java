/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats.c2;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.cryptoregistry.Version;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatKeys;
import com.cryptoregistry.formats.Mode;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonC2KeyFormatter implements FormatKeys {

	protected Curve25519KeyContents c2Keys;
	protected PBEParams pbeParams;

	public JsonC2KeyFormatter(Curve25519KeyContents c2Keys, PBEParams pbeParams) {
		super();
		this.c2Keys = c2Keys;
		this.pbeParams = pbeParams;
		
	}

	/**
	 * Due to the type of keys, Encoding can be null in this case, we always use base64url
	 */
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

		String plain = formatItem(enc, c2Keys);
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
					g.writeObjectFieldStart(c2Keys.handle);
					g.writeStringField("KeyData.Type", "Curve25519");
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
						g.writeObjectFieldStart(c2Keys.handle);
						    g.writeStringField("Encoding", Encoding.Base64url.toString());
							g.writeStringField("P", c2Keys.publicKey.getBase64UrlEncoding());
							g.writeStringField("s", c2Keys.signingPrivateKey.getBase64UrlEncoding());
							g.writeStringField("k", c2Keys.agreementPrivateKey.getBase64UrlEncoding());
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
				g.writeObjectFieldStart(c2Keys.handle);
				  g.writeStringField("Encoding", Encoding.Base64url.toString());
				g.writeStringField("P", c2Keys.publicKey.getBase64UrlEncoding());
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

	private String formatItem(Encoding enc, Curve25519KeyContents item) {
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeObjectFieldStart(c2Keys.handle);
			g.writeStringField("Encoding", Encoding.Base64url.toString());
			g.writeStringField("P", c2Keys.publicKey.getBase64UrlEncoding());
			g.writeStringField("s", c2Keys.signingPrivateKey.getBase64UrlEncoding());
			g.writeStringField("k", c2Keys.agreementPrivateKey.getBase64UrlEncoding());
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
