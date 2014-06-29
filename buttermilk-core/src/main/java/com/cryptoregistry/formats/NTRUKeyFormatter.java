package com.cryptoregistry.formats;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.Encoding;
import com.cryptoregistry.formats.FormatUtil;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.util.MapIterator;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

class NTRUKeyFormatter {

	protected NTRUKeyContents ntruKeys;
	protected final KeyFormat format;
	protected PBEParams pbeParams;

	public NTRUKeyFormatter(NTRUKeyContents ntruKeys) {
		super();
		this.ntruKeys = ntruKeys;
		this.format = ntruKeys.metadata.getFormat();
		this.pbeParams = format.pbeParams;
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

		String plain = formatItem(enc, ntruKeys);
		ArmoredPBEResult result;
		try {
			byte[] plainBytes = plain.getBytes("UTF-8");
			PBE pbe0 = new PBE(pbeParams);
			result = pbe0.encrypt(plainBytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

		g.writeObjectFieldStart(ntruKeys.getDistinguishedHandle());
		g.writeStringField("KeyData.Type", KeyGenerationAlgorithm.NTRU.toString());
		g.writeStringField("KeyData.PBEAlgorithm", pbeParams.getAlg().toString());
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

		g.writeObjectFieldStart(ntruKeys.getDistinguishedHandle());
		g.writeStringField("KeyAlgorithm", KeyGenerationAlgorithm.NTRU.toString());
		g.writeStringField("CreatedOn", TimeUtil.format(ntruKeys.metadata.createdOn));
		//g.writeStringField("Encoding", enc.toString());
		g.writeArrayFieldStart("h");
		for(int item : ntruKeys.h.coeffs) {
			g.writeNumber(item);
		}
		g.writeEndArray();
		
		g.writeEndArray();
		
		NTRUParametersFormatter pFormat = null;
		if(ntruKeys.parameterEnum == null) pFormat = new NTRUParametersFormatter(ntruKeys.params);
		else pFormat = new NTRUParametersFormatter(ntruKeys.parameterEnum);
		pFormat.format(g, writer);
		
		g.writeEndObject();

	}

	protected void formatForPublication(JsonGenerator g, Encoding enc,
			Writer writer) throws JsonGenerationException, IOException {
		
		g.writeObjectFieldStart(ntruKeys.getDistinguishedHandle());
		g.writeStringField("KeyAlgorithm", "EC");
		g.writeStringField("CreatedOn", TimeUtil.format(ntruKeys.metadata.createdOn));
		g.writeStringField("Encoding", enc.toString());
		
		g.writeEndObject();

	}

	private String formatItem(Encoding enc, NTRUKeyContents item) {
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g = null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeStringField("KeyAlgorithm", KeyGenerationAlgorithm.NTRU.toString());
			g.writeStringField("CreatedOn", TimeUtil.format(ntruKeys.metadata.createdOn));
		//	g.writeStringField("Encoding", Encoding.Base10.toString());
		
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
