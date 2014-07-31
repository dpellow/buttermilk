package com.cryptoregistry;

import java.util.List;

import com.cryptoregistry.signature.CryptoSignature;


/**
 * Holders for output of the JSONReader class must implement this interface
 * 
 * @author Dave
 *
 */
public interface KeyMaterials {
	
	String version();
	String regHandle();
	List<CryptoKeyWrapper> keys();
	List<CryptoContact> contacts();
	List<CryptoSignature> signatures();
	List<MapData> mapData();
	List<ListData> listData();
	
}
