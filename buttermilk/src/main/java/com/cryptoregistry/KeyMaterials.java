package com.cryptoregistry;

import java.util.List;

import com.cryptoregistry.signature.CryptoSignature;

public interface KeyMaterials {
	
	String version();
	String regHandle();
	List<CryptoKeyMetadata> keys();
	List<CryptoContact> contacts();
	List<CryptoSignature> signatures();
	List<LocalData> localData();
	List<RemoteData> remoteData();
	

}
