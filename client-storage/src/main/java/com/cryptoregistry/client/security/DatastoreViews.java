/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2015 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.security;

import java.util.Collection;
import java.util.Map;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.ListData;
import com.cryptoregistry.MapData;
import com.cryptoregistry.client.storage.MultiResultCriteria;
import com.cryptoregistry.client.storage.SingleResultCriteria;
import com.cryptoregistry.client.storage.Handle;
import com.cryptoregistry.client.storage.Metadata;
import com.cryptoregistry.client.storage.SingleResult;
import com.cryptoregistry.client.storage.SecureData;
import com.cryptoregistry.signature.CryptoSignature;

public interface DatastoreViews {

	public  Map<Handle, SecureData> getSecureMap();

	public  Map<Handle, Metadata> getMetadataMap();
	
	public  Map<Handle, Metadata> getRegHandleMap();
	
	public  Collection<Metadata> getAllForRegHandle(String regHandle);

	public  void put(String regHandle, CryptoKey key);

	public  void put(String regHandle, CryptoContact contact);

	public  void put(String regHandle, CryptoSignature signature);

	public  void put(String regHandle, MapData local);

	public  void put(String regHandle, ListData remote);
	
	public void get(SingleResultCriteria criteria) throws SuitableMatchFailedException;
	
	public void get(MultiResultCriteria criteria);
	
	public String getDbStatus();
	
	/**
	 * Actual handle, not reg handle here
	 * 
	 * @param handle
	 * @param result
	 * @throws SuitableMatchFailedException
	 */
	public void get(String handle, SingleResult result) throws SuitableMatchFailedException;
	
	


}