/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.security.SecureRandom;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.NamedListProto;
import com.cryptoregistry.protos.Buttermilk.NamedMapProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.SignatureProto;
import com.cryptoregistry.symmetric.AESGCM;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredSortedMap;


public class ButtermilkViews {

	private StoredSortedMap<SecureKey,SecureData> secureMap;
	private SecureRandom rand = new SecureRandom();
	private SensitiveBytes cachedKey;
	
    /**
     * Create the data bindings and collection views.
     */
   
	public ButtermilkViews(ButtermilkDatabase db) {

        ClassCatalog catalog = db.getClassCatalog();
        
        EntryBinding<SecureKey> secureKeyBinding = new SerialBinding<SecureKey>(catalog,SecureKey.class);
        EntryBinding<SecureData> secureDataBinding = new SerialBinding<SecureData>(catalog, SecureData.class);
   
        secureMap = new StoredSortedMap<SecureKey,SecureData>(
				db.getSecureDatabase(),
				secureKeyBinding, 
				secureDataBinding, 
				true);
     
    }

	public StoredSortedMap<SecureKey, SecureData> getSecureMap() {
		return secureMap;
	}
	
	// package protected 
	void setCachedKey(SensitiveBytes cachedKey) {
		this.cachedKey = cachedKey;
	}
	
	void clearCachedKey(){
		cachedKey.selfDestruct();
	}
	
	/**
	 * CRUD interface
	 * 
	 * @param handle
	 * @param proto
	 */
	public void putSecure(String handle, Message proto){
		byte [] input = proto.toByteArray();
		SecureKey key = new SecureKey(handle);
		byte [] iv = new byte [16];
		rand.nextBytes(iv);
		AESGCM gcm = new AESGCM(cachedKey.getData(),iv);
		byte [] encrypted = gcm.encrypt(input);
		SecureData value = new SecureData(encrypted, iv, proto.getClass().getName());
		this.getSecureMap().put(key, value);
	}

	public Message getSecure(String handle) throws InvalidProtocolBufferException{
		SecureData data = this.getSecureMap().get(new SecureKey(handle));
		String className = data.getProtoClass();
		AESGCM gcm = new AESGCM(cachedKey.getData(),data.getIv());
		byte [] bytes = gcm.decrypt(data.getData());
		switch(className){
			case "com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto": {
					return C2KeyContentsProto.parseFrom(bytes);
			}
			case "com.cryptoregistry.protos.Buttermilk.CryptoContactProto": {
				return CryptoContactProto.parseFrom(bytes);
			}
			case "com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto": {
				return ECKeyContentsProto.parseFrom(bytes);
			}
			case "com.cryptoregistry.protos.Buttermilk.NamedMapProto": {
				return NamedMapProto.parseFrom(bytes);
			}
			case "com.cryptoregistry.protos.Buttermilk.NTRUKeyContentsProto": {
				return NamedListProto.parseFrom(bytes);
			}
			case "com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto": {
				return RSAKeyContentsProto.parseFrom(bytes);
			}
			case "com.cryptoregistry.protos.Buttermilk.SignatureProto": {
				return SignatureProto.parseFrom(bytes);
			}
			default: throw new RuntimeException("Unknown proto: "+className);
			
		}

	}
	
	
  
}
