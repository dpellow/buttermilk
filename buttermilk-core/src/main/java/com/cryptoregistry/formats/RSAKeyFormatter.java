package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class RSAKeyFormatter {

	protected final RSAKeyContents rsaKeys;
	protected final KeyFormat format;
	protected final PBEParams pbeParams;

	public RSAKeyFormatter(RSAKeyContents rsaKeys) {
		super();
		this.rsaKeys = rsaKeys;
		this.format = rsaKeys.getFormat();
		this.pbeParams = rsaKeys.getFormat().pbeParams;
	}

	public void formatKeys(JsonGenerator g, Writer writer) {

		try {
			switch (format.mode) {
			case UNSECURED: {
				formatOpen(g, format.encoding, writer);
				break;
			}
			case SECURED: {
				seal(g, format.encoding, writer);
				break;
			}
			case FOR_PUBLICATION: {
				formatForPublication(g, format.encoding, writer);
				break;
			}
			default:
				throw new RuntimeException("Unknown mode");
			}
		}catch(Exception x){
			throw new RuntimeException(x);
		}
		
	}

	protected void seal(JsonGenerator g, Encoding enc, Writer writer)
			throws JsonGenerationException, IOException {

		String plain = formatItem(enc, rsaKeys);
		ArmoredPBEResult result;
		try {
			byte[] plainBytes = plain.getBytes("UTF-8");
			PBE pbe0 = new PBE(pbeParams);
			result = pbe0.encrypt(plainBytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		g.writeObjectFieldStart(rsaKeys.getDistinguishedHandle());
		g.writeStringField("KeyData.Type", "RSA");
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

		g.writeObjectFieldStart(rsaKeys.getDistinguishedHandle());
		g.writeStringField("KeyAlgorithm", "RSA");
		g.writeStringField("CreatedOn", TimeUtil.format(rsaKeys.metadata.createdOn));
		g.writeStringField("Encoding", enc.toString());
		g.writeStringField("Modulus", FormatUtil.wrap(enc, rsaKeys.modulus));
		g.writeStringField("PublicExponent", FormatUtil.wrap(enc, rsaKeys.publicExponent));
		g.writeStringField("PrivateExponent", FormatUtil.wrap(enc, rsaKeys.privateExponent));
		g.writeStringField("P", FormatUtil.wrap(enc, rsaKeys.p));
		g.writeStringField("Q", FormatUtil.wrap(enc, rsaKeys.q));
		g.writeStringField("dP", FormatUtil.wrap(enc, rsaKeys.dP));
		g.writeStringField("dQ", FormatUtil.wrap(enc, rsaKeys.dQ));
		g.writeStringField("qInv", FormatUtil.wrap(enc, rsaKeys.qInv));
		g.writeEndObject();

	}

	protected void formatForPublication(JsonGenerator g, Encoding enc,
			Writer writer) throws JsonGenerationException, IOException {

		g.writeObjectFieldStart(rsaKeys.getDistinguishedHandle());
		g.writeStringField("KeyAlgorithm", "RSA");
		g.writeStringField("CreatedOn", TimeUtil.format(rsaKeys.metadata.createdOn));
		g.writeStringField("Encoding", enc.toString());
		g.writeStringField("Modulus", FormatUtil.wrap(enc, rsaKeys.modulus));
		g.writeStringField("PublicExponent", FormatUtil.wrap(enc, rsaKeys.publicExponent));
		g.writeEndObject();

	}

	private String formatItem(Encoding enc, RSAKeyContents item) {
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeStringField("Handle", rsaKeys.getHandle());
			g.writeStringField("KeyAlgorithm", "RSA");
			g.writeStringField("CreatedOn", TimeUtil.format(rsaKeys.metadata.createdOn));
			g.writeStringField("Encoding", enc.toString());
			g.writeStringField("Modulus", FormatUtil.wrap(enc, rsaKeys.modulus));
			g.writeStringField("PublicExponent", FormatUtil.wrap(enc, rsaKeys.publicExponent));
			g.writeStringField("PrivateExponent", FormatUtil.wrap(enc, rsaKeys.privateExponent));
			g.writeStringField("P", FormatUtil.wrap(enc, rsaKeys.p));
			g.writeStringField("Q", FormatUtil.wrap(enc, rsaKeys.q));
			g.writeStringField("dP", FormatUtil.wrap(enc, rsaKeys.dP));
			g.writeStringField("dQ", FormatUtil.wrap(enc, rsaKeys.dQ));
			g.writeStringField("qInv", FormatUtil.wrap(enc, rsaKeys.qInv));
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
