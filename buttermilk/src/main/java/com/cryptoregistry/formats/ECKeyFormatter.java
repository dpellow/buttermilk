package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class ECKeyFormatter {

	protected ECKeyContents ecKeys;
	protected PBEParams pbeParams;

	public ECKeyFormatter(ECKeyContents ecKeys) {
		super();
		this.ecKeys = ecKeys;
		this.pbeParams = ecKeys.getFormat().pbeParams;
	}

	public void formatKeys(Writer writer) {

		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();

			switch (ecKeys.getFormat().mode) {
			case OPEN: {
				formatOpen(g, ecKeys.getFormat().encoding, writer);
				break;
			}
			case SEALED: {
				seal(g, ecKeys.getFormat().encoding, writer);
				break;
			}
			case FOR_PUBLICATION: {
				formatForPublication(g, ecKeys.getFormat().encoding, writer);
				break;
			}
			default:
				throw new RuntimeException("Unknown mode");
			}
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

	protected void seal(JsonGenerator g, Encoding enc, Writer writer)
			throws JsonGenerationException, IOException {

		String plain = formatItem(enc, ecKeys);
		ArmoredPBEResult result;
		try {
			byte[] plainBytes = plain.getBytes("UTF-8");
			PBE pbe0 = new PBE(pbeParams);
			result = pbe0.encrypt(plainBytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		g.writeObjectFieldStart(ecKeys.getHandle());
		g.writeStringField("KeyData.Type", "EC");
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

	protected void formatOpen(JsonGenerator g, Encoding enc, Writer writer)
			throws JsonGenerationException, IOException {

		g.writeObjectFieldStart(ecKeys.getHandle());
		g.writeStringField("Encoding", enc.toString());
		g.writeStringField("Q", FormatUtil.serializeECPoint(ecKeys.Q, enc));
		g.writeStringField("D", FormatUtil.wrap(enc, ecKeys.d));
		g.writeStringField("CurveName", ecKeys.curveName);
		g.writeEndObject();

	}

	protected void formatForPublication(JsonGenerator g, Encoding enc,
			Writer writer) throws JsonGenerationException, IOException {

		g.writeObjectFieldStart(ecKeys.getHandle());
		g.writeStringField("Encoding", enc.toString());
		g.writeStringField("Q", FormatUtil.serializeECPoint(ecKeys.Q, enc));
		g.writeStringField("CurveName", ecKeys.curveName);
		g.writeEndObject();

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
