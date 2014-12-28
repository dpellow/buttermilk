/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.formats;

import java.io.File;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import x.org.bouncycastle.math.ec.ECPoint;
import x.org.bouncycastle.pqc.crypto.ntru.NTRUEncryptionParameters;
import x.org.bouncycastle.pqc.math.ntru.polynomial.DenseTernaryPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.IntegerPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.Polynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.ProductFormPolynomial;
import x.org.bouncycastle.pqc.math.ntru.polynomial.SparseTernaryPolynomial;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.CryptoKeyWrapperImpl;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.MapData;
import com.cryptoregistry.ListData;
import com.cryptoregistry.SignatureAlgorithm;
import com.cryptoregistry.c2.key.AgreementPrivateKey;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.c2.key.SigningPrivateKey;
import com.cryptoregistry.ec.ECCustomParameters;
import com.cryptoregistry.ec.ECF2MCustomParameters;
import com.cryptoregistry.ec.ECFPCustomParameters;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUKeyForPublication;
import com.cryptoregistry.ntru.NTRUKeyMetadata;
import com.cryptoregistry.ntru.NTRUNamedParameters;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.signature.C2CryptoSignature;
import com.cryptoregistry.signature.C2Signature;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.ECDSACryptoSignature;
import com.cryptoregistry.signature.ECDSASignature;
import com.cryptoregistry.signature.RSACryptoSignature;
import com.cryptoregistry.signature.RSASignature;
import com.cryptoregistry.signature.SignatureMetadata;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.cryptoregistry.symmetric.SymmetricKeyMetadata;
import com.cryptoregistry.util.ArmoredCompressedString;
import com.cryptoregistry.util.ArmoredString;
import com.cryptoregistry.util.ArrayUtil;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Read the canonical format as output by JSONBuilder. This reader is for the scenario where
 * nothing is known at runtime about the contents of the JSON being passed in
 * 
 * @author Dave
 * @see JSONFormatter
 * 
 */
public class JSONReader {
	
	protected final ObjectMapper mapper;
	protected final Map<String,Object> map;
	
	/**
	 * Fails immediately if anything goes wrong reading the file or parsing
	 * @param path
	 */
	@SuppressWarnings("unchecked")
	public JSONReader(File path) {
		
		// TODO the below is reasonable, but use the stream parsing API to be more efficient
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(path, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONReader(Reader in) {
		
		// TODO the below is reasonable, but someday use the stream parsing API to be more efficient
		mapper = new ObjectMapper();
		try {
			map = mapper.readValue(in, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public KeyMaterials parse() {
		
		KeyMaterials km = new KeyMaterials() {

			@Override
			public String version() {
				return String.valueOf(map.get("Version"));
			}

			@Override
			public String regHandle() {
				return String.valueOf(map.get("RegHandle"));
			}

			/**
			 * Create a list of CryptoKeyWrappers
			 */
			@SuppressWarnings("unchecked")
			@Override
			public List<CryptoKeyWrapper> keys() {
				
				ArrayList<CryptoKeyWrapper> list = new ArrayList<CryptoKeyWrapper>();
				
				// returns a map with the key uuids as keys
				
				Map<String, Object> uuids = (Map<String, Object>) map.get("Keys");
				Iterator<String> iter = uuids.keySet().iterator();
				while(iter.hasNext()) {
					
					CryptoKeyWrapper wrapper = null;
					
					String distinguishedKey = iter.next();
					Map<String, Object> keyData = (Map<String, Object>) uuids.get(distinguishedKey);
					
					// key is for publication
					if(distinguishedKey.endsWith("-P")){
						// key metadata - 
						String handle = distinguishedKey.substring(0,distinguishedKey.length()-2);
						Date createdOn = TimeUtil.getISO8601FormatDate(String.valueOf(keyData.get("CreatedOn")));
						EncodingHint encoding = EncodingHint.valueOf((String)keyData.get("Encoding"));
						String keyAlgorithm = (String) keyData.get("KeyAlgorithm");
						Mode mode = Mode.FOR_PUBLICATION;
						KeyFormat format = new KeyFormat(encoding,mode);
						
						// define metadata for key
						CryptoKeyMetadata meta = null;
						KeyGenerationAlgorithm k = KeyGenerationAlgorithm.valueOf(keyAlgorithm);
						switch(k){
							
							case Curve25519: {
								meta = new C2KeyMetadata(handle,createdOn,format);
								ArmoredString P = new ArmoredString(String.valueOf(keyData.get("P")));
								PublicKey key = new PublicKey(P.decodeToBytes());
								Curve25519KeyForPublication p = new Curve25519KeyForPublication((C2KeyMetadata) meta,key);
								list.add(new CryptoKeyWrapperImpl(p));
								break;
							}
							case EC: {
								meta = new ECKeyMetadata(handle,createdOn,format);
								String curveName = String.valueOf(keyData.get("CurveName"));
								String qIn = String.valueOf(keyData.get("Q"));
								ECPoint q=FormatUtil.parseECPoint(curveName, encoding, qIn);
								ECKeyForPublication p=new ECKeyForPublication((ECKeyMetadata)meta,q,curveName);
								list.add(new CryptoKeyWrapperImpl(p));
								break;
							}
							case NTRU: {
								NTRUEncryptionParameters params = null;
								String paramName = null;
								meta = new NTRUKeyMetadata(handle,createdOn,format);
								ArmoredCompressedString _h = new ArmoredCompressedString(String.valueOf(keyData.get("h")));
								IntegerPolynomial h = new IntegerPolynomial(ArrayUtil.unwrapIntArray(_h));
							//	ArmoredCompressedString _fp = new ArmoredCompressedString(String.valueOf(keyData.get("fp")));
							//	IntegerPolynomial fp = new IntegerPolynomial(ArrayUtil.unwrapIntArray(_fp));
								if(keyData.containsKey("NTRUParams")){
									// parse params
									Map<String,Object> inner = (Map<String,Object>)keyData.get("NTRUParams");
									NTRUParametersReader reader = new NTRUParametersReader(inner);
									params = reader.parse();
								}else{
									paramName = String.valueOf(keyData.get("NamedParameters"));
								//	params = NTRUNamedParameters.valueOf(paramName).getParameters();
								}
								
								NTRUKeyForPublication key = null;
								
								if(paramName == null) {
									key = new NTRUKeyForPublication((NTRUKeyMetadata)meta,params,h);
								}else{
									NTRUNamedParameters e = NTRUNamedParameters.valueOf(paramName);
									key = new NTRUKeyForPublication((NTRUKeyMetadata)meta,e,h);
								}
								
								list.add(new CryptoKeyWrapperImpl(key));
								
								break;
							}
							case RSA: {
								meta = new RSAKeyMetadata(handle,createdOn,format);
								BigInteger modulus = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("Modulus")));
								BigInteger publicExponent = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("PublicExponent")));
								RSAKeyForPublication rPub = new RSAKeyForPublication((RSAKeyMetadata)meta,modulus,publicExponent);
								list.add(new CryptoKeyWrapperImpl(rPub));
								break;
							}
							case DSA: {
								// TODO
								break;
							}
							case Symmetric:{
								throw new RuntimeException("Key has no public form: "+keyAlgorithm);
							}
							default : throw new RuntimeException("Unknown alg: "+keyAlgorithm);
						}
						
					}else if(distinguishedKey.endsWith("-S")){
						
						//final String keyAlgorithm = (String) keyData.get("KeyData.Type");
						final ArmoredPBEResult wrapped = PBEAlg.loadFrom(keyData);
						wrapper = new CryptoKeyWrapperImpl(wrapped);
						list.add(wrapper);
						
					}else if(distinguishedKey.endsWith("-U")){
						// key metadata - 
						String handle = distinguishedKey.substring(0,distinguishedKey.length()-2);
						Date createdOn = TimeUtil.getISO8601FormatDate((String) keyData.get("CreatedOn"));
						EncodingHint encoding = EncodingHint.valueOf((String)keyData.get("Encoding"));
						String keyAlgorithm = (String) keyData.get("KeyAlgorithm");
						Mode mode = Mode.UNSECURED;
						KeyFormat format = new KeyFormat(encoding,mode);
						
						// define metadata for key
						CryptoKeyMetadata meta = null;
						KeyGenerationAlgorithm k = KeyGenerationAlgorithm.valueOf(keyAlgorithm);
						switch(k){
							case Symmetric:{
								meta = new SymmetricKeyMetadata(handle,createdOn,format);
								ArmoredString s = new ArmoredString(String.valueOf(keyData.get("s")));
								SymmetricKeyContents contents = new SymmetricKeyContents((SymmetricKeyMetadata)meta,s.decodeToBytes());
								list.add(new CryptoKeyWrapperImpl(contents));
							}
							case Curve25519: {
								meta = new C2KeyMetadata(handle,createdOn,format);
								ArmoredString P = new ArmoredString(String.valueOf(keyData.get("P")));
								ArmoredString k_ = new ArmoredString(String.valueOf(keyData.get("k")));
								ArmoredString s = new ArmoredString(String.valueOf(keyData.get("s")));
								PublicKey pKey = new PublicKey(P.decodeToBytes());
								SigningPrivateKey sPrivKey = new SigningPrivateKey(k_.decodeToBytes());
								AgreementPrivateKey aPrivKey = new AgreementPrivateKey(s.decodeToBytes());
								Curve25519KeyContents p = new Curve25519KeyContents((C2KeyMetadata) meta,pKey,sPrivKey,aPrivKey);
								list.add(new CryptoKeyWrapperImpl(p));
								break;
							}
							case EC: {
								meta = new ECKeyMetadata(handle,createdOn,format);
								String curveName = (String)keyData.get("CurveName");
								ECCustomParameters params = null;
								if(curveName != null){
									String qIn = String.valueOf(keyData.get("Q"));
									ECPoint q=FormatUtil.parseECPoint(curveName, encoding, qIn);
									BigInteger d = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("D")));
									ECKeyContents p=new ECKeyContents((ECKeyMetadata)meta,q,curveName,d);
									list.add(new CryptoKeyWrapperImpl(p));
								}else{
									
									// support for custom curve definitions
									Map<String,Object> def = (Map<String,Object>) keyData.get("Curve");
									LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
									Iterator<String> _iter = def.keySet().iterator();
									while(_iter.hasNext()){
										String key = _iter.next();
										map.put(key, String.valueOf(def.get(key)));
									}
									
									if(def.get("Field").equals("FP")){
										params = new ECFPCustomParameters(null,map);
									}else if(def.get("Field").equals("F2M")){
										params = new ECF2MCustomParameters(null,map);
									}
									
									EncodingHint enc = EncodingHint.valueOf(String.valueOf(keyData.get("Encoding")));
									ECPoint q = FormatUtil.parseECPoint(params.getParameters().getCurve(), enc, String.valueOf(keyData.get("Q")));
									BigInteger D = FormatUtil.unwrap(enc, String.valueOf(keyData.get("D")));
									
									ECKeyContents p=new ECKeyContents((ECKeyMetadata)meta,q,params,D);
									list.add(new CryptoKeyWrapperImpl(p));
									
								}
								break;
							}
							case NTRU: {
								NTRUEncryptionParameters params = null;
								String paramName = null;
								meta = new NTRUKeyMetadata(handle,createdOn,format);
								ArmoredCompressedString _h = new ArmoredCompressedString(String.valueOf(keyData.get("h")));
								IntegerPolynomial h = new IntegerPolynomial(ArrayUtil.unwrapIntArray(_h));
								ArmoredCompressedString _fp = new ArmoredCompressedString(String.valueOf(keyData.get("fp")));
								IntegerPolynomial fp = new IntegerPolynomial(ArrayUtil.unwrapIntArray(_fp));
								
								Polynomial t = null;
								
								if(keyData.containsKey("t0")){
									// product form
									int [] t0 = ArrayUtil.unwrapIntArray(
											new ArmoredCompressedString(String.valueOf(keyData.get("t0")))
									);
									int [] t1 = ArrayUtil.unwrapIntArray(
											new ArmoredCompressedString(String.valueOf(keyData.get("t1")))
									);
									int [] t2 = ArrayUtil.unwrapIntArray(
											new ArmoredCompressedString(String.valueOf(keyData.get("t2")))
									);
									
									t = new ProductFormPolynomial(
											new SparseTernaryPolynomial(t0),
											new SparseTernaryPolynomial(t1),
											new SparseTernaryPolynomial(t2)
									);
									
								}else if(keyData.containsKey("td")){
									// dense ternary 
									int [] td = ArrayUtil.unwrapIntArray(
											new ArmoredCompressedString(String.valueOf(keyData.get("td")))
									);
									
									t = new DenseTernaryPolynomial(td);
									
								}else if(keyData.containsKey("ts")){
									// sparse ternary 
									int [] td = ArrayUtil.unwrapIntArray(
											new ArmoredCompressedString(String.valueOf(keyData.get("ts")))
									);
									
									t = new SparseTernaryPolynomial(td);
									
								}
								
								
								if(keyData.containsKey("NTRUParams")){
									// parse params
									Map<String,Object> inner = (Map<String,Object>)keyData.get("NTRUParams");
									NTRUParametersReader reader = new NTRUParametersReader(inner);
									params = reader.parse();
								}else{
									paramName = String.valueOf(keyData.get("NamedParameters"));
								//	params = NTRUNamedParameters.valueOf(paramName).getParameters();
								}
								
								NTRUKeyContents key = null;
								
								if(paramName == null) {
									key = new NTRUKeyContents((NTRUKeyMetadata)meta,params,h,t,fp);
								}else{
									NTRUNamedParameters e = NTRUNamedParameters.valueOf(paramName);
									key = new NTRUKeyContents((NTRUKeyMetadata)meta,e,h,t,fp);
								}
								
								list.add(new CryptoKeyWrapperImpl(key));
								
								break;
							}
							case RSA: {
								meta = new RSAKeyMetadata(handle,createdOn,format);
								BigInteger modulus = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("Modulus")));
								BigInteger publicExponent = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("PublicExponent")));
								BigInteger privateExponent = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("PrivateExponent")));
								BigInteger P = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("P")));
								BigInteger Q = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("Q")));
								BigInteger dP = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("dP")));
								BigInteger dQ = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("dQ")));
								BigInteger qInv = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("qInv")));
								RSAKeyContents rPub = new RSAKeyContents(
										(RSAKeyMetadata)meta,
										modulus,
										publicExponent,
										privateExponent,
										P,
										Q,
										dP,
										dQ,
										qInv);
								list.add(new CryptoKeyWrapperImpl(rPub));
								break;
							}
							case DSA: {
								// TODO
								break;
							}
							default : throw new RuntimeException("Unknown alg: "+keyAlgorithm);
						}
						
					}else{
						throw new RuntimeException("Unclear what the handle denotes, expected -P,-S,-U at end");
					}
				}
				
				return list;
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<CryptoContact> contacts() {
				
				ArrayList<CryptoContact> list = new ArrayList<CryptoContact>();
				
				Map<String, Object> uuids = (Map<String, Object>) map.get("Contacts");
				Iterator<String> iter = uuids.keySet().iterator();
				while(iter.hasNext()) {
					String handle = iter.next();
					Map<String, Object> contactData = (Map<String, Object>) uuids.get(handle);
					list.add(new CryptoContact(handle,contactData));
				}
				return list;
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<CryptoSignature> signatures() {
				ArrayList<CryptoSignature> list = new ArrayList<CryptoSignature>();
				Map<String, Object> uuids = (Map<String, Object>) map.get("Signatures");
				Iterator<String> iter = uuids.keySet().iterator();
				while(iter.hasNext()) {
					String handle = iter.next();
					Map<String, Object> sigData = (Map<String, Object>) uuids.get(handle);
					SignatureAlgorithm sigAlg = SignatureAlgorithm.valueOf(
							String.valueOf(sigData.get("SignatureAlgorithm")));
					// common to all
					Date createdOn = TimeUtil.getISO8601FormatDate(String.valueOf(sigData.get("CreatedOn")));
					String signedWith = String.valueOf(sigData.get("SignedWith"));
					String signedBy = String.valueOf(sigData.get("SignedBy"));
					String digestAlg = String.valueOf(sigData.get("DigestAlgorithm"));
					SignatureMetadata meta = 
							new SignatureMetadata(handle,createdOn,sigAlg,digestAlg,signedWith,signedBy);
					List<String> dataRefs = collect(String.valueOf(sigData.get("DataRefs")));
					
					// specific to the encoding of each CryptoSignature subclass
					switch(sigAlg){
						case RSA: {
							String sval = String.valueOf(sigData.get("s"));
							RSASignature sig = new RSASignature(new ArmoredString(sval));
							list.add(new RSACryptoSignature(meta,dataRefs,sig));
							break;
						}
						case ECDSA: {
							BigInteger r = new BigInteger(String.valueOf(sigData.get("r")), 16);
							BigInteger s = new BigInteger(String.valueOf(sigData.get("s")), 16);
							
							ECDSASignature sig = new ECDSASignature(r,s);
							list.add(new ECDSACryptoSignature(meta,dataRefs,sig));
							break;
						}
						case ECKCDSA: {
							ArmoredString v = new ArmoredString(String.valueOf(sigData.get("v")));
							ArmoredString s = new ArmoredString(String.valueOf(sigData.get("s")));
							C2Signature sig = new C2Signature(v,s);
							list.add(new C2CryptoSignature(meta,dataRefs,sig));
							break;
						}
						default: {
							throw new RuntimeException("Unknown SignatureAlgorithm: "+sigAlg);
						}
					}
				}
				return list;
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<MapData> mapData() {
				
				ArrayList<MapData> list = new ArrayList<MapData>();
				
				Map<String, Object> data = (Map<String, Object>) map.get("Data");
				
				Map<String, Object> uuids = (Map<String, Object>) data.get("Local");
				Iterator<String> iter = uuids.keySet().iterator();
				while(iter.hasNext()) {
					String handle = iter.next();
					Map<String, String> localData = (Map<String, String>) uuids.get(handle);
					list.add(new MapData(handle,localData));
				}
					
				return list;
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<ListData> listData() {
				ArrayList<ListData> list = new ArrayList<ListData>();
				
				Map<String, Object> data = (Map<String, Object>) map.get("Data");
				
				List<Object> urls = (List<Object>) data.get("Remote");
				ListData rd = new ListData();
				for(Object url: urls){
					rd.addURL(String.valueOf(url));
				}
				
				list.add(rd);
					
				return list;
			}
			
		};
		
		return km;
	}
	
	private List<String> collect(String in) {
		List<String> list = new ArrayList<String>();
		String [] items = in.split("\\.");
		for(String ref: items){
			list.add(ref.trim());
		}
		return list;
	}

}
