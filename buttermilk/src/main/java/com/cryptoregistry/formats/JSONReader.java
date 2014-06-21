package com.cryptoregistry.formats;

import java.io.File;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import x.org.bouncycastle.math.ec.ECPoint;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.CryptoKeyWrapperImpl;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.LocalData;
import com.cryptoregistry.RemoteData;
import com.cryptoregistry.c2.key.AgreementPrivateKey;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.PublicKey;
import com.cryptoregistry.c2.key.SigningPrivateKey;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.util.ArmoredString;
import com.cryptoregistry.util.TimeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Read the canonical format as output by JSONBuilder
 * 
 * @author Dave
 * @see JSONBuilder
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
		
		// TODO the below is reasonable, but use the stream parsing API to be more efficient
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
						Encoding encoding = Encoding.valueOf((String)keyData.get("Encoding"));
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
						Encoding encoding = Encoding.valueOf((String)keyData.get("Encoding"));
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
								String curveName = String.valueOf(keyData.get("CurveName"));
								String qIn = String.valueOf(keyData.get("Q"));
								ECPoint q=FormatUtil.parseECPoint(curveName, encoding, qIn);
								BigInteger d = FormatUtil.unwrap(encoding, String.valueOf(keyData.get("D")));
								ECKeyContents p=new ECKeyContents((ECKeyMetadata)meta,q,curveName,d);
								list.add(new CryptoKeyWrapperImpl(p));
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
						throw new RuntimeException("Unclear what the handle denotes, expected -P,-S,-U");
					}
				}
				
				return list;
			}

			@Override
			public List<CryptoContact> contacts() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<CryptoSignature> signatures() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<LocalData> localData() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<RemoteData> remoteData() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		return km;
	}

}
