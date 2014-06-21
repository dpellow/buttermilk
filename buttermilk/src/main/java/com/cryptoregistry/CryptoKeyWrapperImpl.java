package com.cryptoregistry;

import java.util.Iterator;
import java.util.Map;

import com.cryptoregistry.formats.C2KeyFormatReader;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.pbe.ArmoredPBEResult;
import com.cryptoregistry.pbe.ArmoredPBKDF2Result;
import com.cryptoregistry.pbe.ArmoredScryptResult;
import com.cryptoregistry.pbe.PBE;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.pbe.PBEParams;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CryptoKeyWrapperImpl implements CryptoKeyWrapper {

	private Object wrapped;

	public CryptoKeyWrapperImpl(Object wrapped) {
		super();
		this.wrapped = wrapped;
	}

	@Override
	public CryptoKeyMetadata getMetadata() {
		if(!(wrapped instanceof ArmoredPBEResult)){
			return (CryptoKeyMetadata) wrapped;
		}else{
			return null;
		}
	}

	@Override
	public Class<?> getWrappedType() {
		return wrapped.getClass();
	}

	@Override
	public boolean isForPublication() {
		// TODO Auto-generated method stub
		return wrapped instanceof Verifier && !(wrapped instanceof Signer);
	}

	@Override
	public boolean isSecure() {
		
		return wrapped instanceof ArmoredPBEResult;
	}

	@Override
	public boolean unlock(Password password) {
		if(wrapped instanceof ArmoredPBEResult){
			PBEParams params = null;
			byte [] data = null;
			if(wrapped instanceof ArmoredPBKDF2Result){
				ArmoredPBKDF2Result res = (ArmoredPBKDF2Result) wrapped;
				params = new PBEParams(PBEAlg.PBKDF2);
				params.setSalt(res.getSaltWrapper());
				params.setIterations(res.iterations);
				params.setPassword(password);
				PBE pbe0 = new PBE(params);
				data = pbe0.decrypt(res.getResultBytes());
				
			}else{
				ArmoredScryptResult res = (ArmoredScryptResult) wrapped;
				params = new PBEParams(PBEAlg.SCRYPT);
				params.setSalt(res.getSaltWrapper());
				params.setIv(res.getIVWrapper());
				params.setSalt(res.getSaltWrapper());
				params.setCpuMemoryCost_N(res.cpuMemoryCost);
				params.setParallelization_p(res.parallelization);
				params.setPassword(password);
				PBE pbe0 = new PBE(params);
				data = pbe0.decrypt(res.getResultBytes());
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String keyAlgorithm = null;
			try {
				// top level map
				@SuppressWarnings("unchecked")
				Map<String,Object> map = (Map<String,Object>) mapper.readValue(data, Map.class);
				// drill down to get key type
				Iterator<String> iter = map.keySet().iterator();
				if(iter.hasNext()){
					String distUUID = iter.next();
					@SuppressWarnings("unchecked")
					Map<String,Object> inner = (Map<String,Object>) map.get(distUUID);
					keyAlgorithm = String.valueOf(inner.get("KeyAlgorithm"));
				}
				
				KeyGenerationAlgorithm alg = KeyGenerationAlgorithm.valueOf(keyAlgorithm);
				switch(alg){
					case Curve25519: {
						C2KeyFormatReader reader = new C2KeyFormatReader(map);
						wrapped = reader.read();
					}
					case EC:{
						
					}
					case RSA:{
						
					}
					default: throw new RuntimeException("Unknown KeyGenerationAlgorithm: "+keyAlgorithm);
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}else{
			return false;
		}
	}

	@Override
	public void lock(KeyFormat format) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getKeyContents() {
		return wrapped;
	}

	@Override
	public void setKeyContents(Object obj) {
		this.wrapped = obj;
	}

}
