package com.cyptoregistry.formats;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;

import net.iharder.Base64;

import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEParams;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class JsonRSAFormatter {

	private RSAKeyContents rsaKeys;
	private PBEParams pbeParams;
	
	public JsonRSAFormatter(RSAKeyContents rsaKeys, PBEParams pbeParams) {
		super();
		this.rsaKeys = rsaKeys;
		this.pbeParams = pbeParams;
	}

	public void formatKeys(Mode mode, Encoding enc, Writer writer){
	
		switch(mode){
			case EXPOSED: {
				formatExposed(enc,writer);
				break;
			}
			case SEALED: {
				formatSealed(enc,writer);
				break;
			}
			case PUBLISHED: {
				formatKeyExchange(enc,writer);
				break;
			}
			default: throw new RuntimeException("Unknown mode");
		}
		
	}
	
	@SuppressWarnings("resource")
	protected void formatSealed(Encoding enc, Writer writer){
		StringWriter privateDataWriter = new StringWriter();
		JsonFactory f = new JsonFactory();
		JsonGenerator g=null;
		try {
			g = f.createGenerator(privateDataWriter);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
				g.writeStringField("Handle", rsaKeys.handle);
				g.writeStringField("Encoding", enc.toString());
				g.writeStringField("Modulus", wrap(enc,rsaKeys.modulus));
				g.writeStringField("PublicExponent", wrap(enc, rsaKeys.publicExponent));
				g.writeStringField("PrivateExponent", wrap(enc,rsaKeys.privateExponent));
				g.writeStringField("P", wrap(enc,rsaKeys.p));
				g.writeStringField("Q", wrap(enc,rsaKeys.q));
				g.writeStringField("dP", wrap(enc,rsaKeys.dP));
				g.writeStringField("dQ", wrap(enc,rsaKeys.dQ));
				g.writeStringField("qInv", wrap(enc,rsaKeys.qInv));
			g.writeEndObject(); 
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				g.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String plain = privateDataWriter.toString();
		ArmoredPBEResult result;
		try {
			byte [] plainBytes = plain.getBytes("UTF-8");
			PBE pbe0 = new PBE(pbeParams);
			result = pbe0.encrypt(plainBytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		if(result instanceof ArmoredPBKDF2Result){
			try {
				g = f.createGenerator(writer);
				g.useDefaultPrettyPrinter();
				g.writeStartObject();
				g.writeStringField("Version", "Buttermilk RSA Keys 1.0");
				g.writeStringField("CreatedOn", TimeUtil.now());
				g.writeArrayFieldStart("Keys");
					g.writeStartObject();
					g.writeStringField("KeyData.EncryptedData", result.base64Enc);
					g.writeStringField("KeyData.PBEAlgorithm", pbeParams.getAlg().toString());
					g.writeStringField("KeyData.PBESalt", result.base64Salt);
					g.writeStringField("KeyData.Iterations", String.valueOf(((ArmoredPBKDF2Result) result).iterations));
					g.writeEndObject(); 
				g.writeEndArray();
				g.writeEndObject(); 
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					g.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if(result instanceof ArmoredScryptResult){
			try {
				g = f.createGenerator(writer);
				g.useDefaultPrettyPrinter();
				g.writeStartObject();
				g.writeStringField("Version", "Buttermilk RSA Keys 1.0");
				g.writeStringField("CreatedOn", TimeUtil.now());
				g.writeArrayFieldStart("Keys");
					g.writeStartObject();
					g.writeStringField("KeyData.EncryptedData", result.base64Enc);
					g.writeStringField("KeyData.PBEAlgorithm", pbeParams.getAlg().toString());
					g.writeStringField("KeyData.PBESalt", result.base64Salt);
					g.writeStringField("KeyData.IV",((ArmoredScryptResult) result).base64IV);
					g.writeStringField("KeyData.BlockSize",String.valueOf(((ArmoredScryptResult) result).blockSize));
					g.writeStringField("KeyData.CpuMemoryCost",String.valueOf(((ArmoredScryptResult) result).cpuMemoryCost));
					g.writeStringField("KeyData.Parallelization",String.valueOf(((ArmoredScryptResult) result).parallelization));
					g.writeEndObject(); 
				g.writeEndArray();
				g.writeEndObject(); 
				g.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			throw new RuntimeException("Sorry I don't recognize this class: "+result.getClass().getName());
		}
	}


	protected void formatExposed(Encoding enc, Writer writer){
		JsonFactory f = new JsonFactory();
		JsonGenerator g;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeStringField("Version", "Buttermilk RSA Keys 1.0");
			g.writeStringField("CreatedOn", TimeUtil.now());
			
			g.writeArrayFieldStart("Keys");
				g.writeStartObject();
				g.writeStringField("Handle", rsaKeys.handle);
				g.writeStringField("Encoding", enc.toString());
				g.writeStringField("Modulus", wrap(enc,rsaKeys.modulus));
				g.writeStringField("PublicExponent", wrap(enc, rsaKeys.publicExponent));
				g.writeStringField("PrivateExponent", wrap(enc,rsaKeys.privateExponent));
				g.writeStringField("P", wrap(enc,rsaKeys.p));
				g.writeStringField("Q", wrap(enc,rsaKeys.q));
				g.writeStringField("dP", wrap(enc,rsaKeys.dP));
				g.writeStringField("dQ", wrap(enc,rsaKeys.dQ));
				g.writeStringField("qInv", wrap(enc,rsaKeys.qInv));
				g.writeEndObject(); 
			g.writeEndArray();
			g.writeEndObject(); 
			g.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void formatKeyExchange(Encoding enc, Writer writer){
		JsonFactory f = new JsonFactory();
		JsonGenerator g;
		try {
			g = f.createGenerator(writer);
			g.useDefaultPrettyPrinter();
			g.writeStartObject();
			g.writeStringField("Version", "Buttermilk RSA Keys 1.0");
			g.writeStringField("CreatedOn", TimeUtil.now());
			
			g.writeArrayFieldStart("Keys");
				g.writeStartObject();
				g.writeStringField("Handle", rsaKeys.handle);
				g.writeStringField("Encoding", enc.toString());
				g.writeStringField("Modulus", wrap(enc,rsaKeys.modulus));
				g.writeStringField("PublicExponent", wrap(enc, rsaKeys.publicExponent));
				g.writeEndObject(); 
			g.writeEndArray();
			g.writeEndObject(); 
			g.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String wrap(Encoding enc, BigInteger bi){
		switch(enc){
			case Base2: return bi.toString(2);
			case Base10: return bi.toString(10);
			case Base16: return bi.toString(16);
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
			default: throw new RuntimeException("Unknown encoding: "+enc);
		}
	}

}
