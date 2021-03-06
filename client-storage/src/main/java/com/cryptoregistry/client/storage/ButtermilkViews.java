/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.MapData;
import com.cryptoregistry.ListData;
import com.cryptoregistry.Signer;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.client.security.DatastoreViews;
import com.cryptoregistry.client.security.SuitableMatchFailedException;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ntru.NTRUKeyContents;
import com.cryptoregistry.ntru.NTRUKeyForPublication;
import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.builder.C2KeyContentsProtoBuilder;
import com.cryptoregistry.proto.builder.C2KeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.builder.ContactProtoBuilder;
import com.cryptoregistry.proto.builder.ECKeyContentsProtoBuilder;
import com.cryptoregistry.proto.builder.ECKeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.builder.NTRUKeyContentsProtoBuilder;
import com.cryptoregistry.proto.builder.NTRUKeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.builder.NamedListProtoBuilder;
import com.cryptoregistry.proto.builder.NamedMapProtoBuilder;
import com.cryptoregistry.proto.builder.RSAKeyContentsProtoBuilder;
import com.cryptoregistry.proto.builder.RSAKeyForPublicationProtoBuilder;
import com.cryptoregistry.proto.builder.SignatureProtoBuilder;
import com.cryptoregistry.proto.builder.SymmetricKeyContentsProtoBuilder;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.NTRUKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.NTRUKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.SymmetricKeyContentsProto;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.symmetric.AESCBCPKCS7;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.EnvironmentStats;

public class ButtermilkViews implements DatastoreViews {

	private ButtermilkBDBDatabase db;
	private final StoredSortedMap<Handle, SecureData> secureMap;
	private final StoredSortedMap<Handle, Metadata> metadataMap;
	private final StoredSortedMap<Handle, Metadata> regHandleMap;
	private final SecureRandom rand = new SecureRandom();
	private final SensitiveBytes cachedKey;

	/**
	 * Create the data bindings and collection views.
	 */

	public ButtermilkViews(ButtermilkBDBDatabase db, SensitiveBytes cachedKey) {

		this.cachedKey = cachedKey;
		this.db = db;
		ClassCatalog catalog = db.getClassCatalog();

		EntryBinding<Handle> secureKeyBinding = new SerialBinding<Handle>(
				catalog, Handle.class);
		EntryBinding<SecureData> secureDataBinding = new SerialBinding<SecureData>(
				catalog, SecureData.class);

		EntryBinding<Handle> metadataKeyBinding = new SerialBinding<Handle>(
				catalog, Handle.class);
		EntryBinding<Metadata> metadataDataBinding = new SerialBinding<Metadata>(
				catalog, Metadata.class);
		
		// this is the reg Handle
		EntryBinding<Handle> regHandleKeyBinding = new SerialBinding<Handle>(
				catalog, Handle.class);
		EntryBinding<Metadata> regHandleDataBinding = new SerialBinding<Metadata>(
				catalog, Metadata.class);

		secureMap = new StoredSortedMap<Handle, SecureData>(
				db.getSecureDatabase(), secureKeyBinding, secureDataBinding,
				true);

		metadataMap = new StoredSortedMap<Handle, Metadata>(
				db.getMetadataDatabase(), metadataKeyBinding,
				metadataDataBinding, true);
		
		regHandleMap = new StoredSortedMap<Handle, Metadata>(
				db.getRegHandleDatabase(), regHandleKeyBinding,
				regHandleDataBinding, true);

	}

	@Override
	public Map<Handle, SecureData> getSecureMap() {
		return secureMap;
	}

	@Override
	public Map<Handle, Metadata> getMetadataMap() {
		return metadataMap;
	}
	
	public Map<Handle, Metadata> getRegHandleMap() {
		return regHandleMap;
	}
	
	/**
	 * Access to the index
	 */
	public Collection<Metadata> getAllForRegHandle(String regHandle){
		return regHandleMap.duplicates(new Handle(regHandle));
	}
	
	public boolean hasRegHandle(String regHandle){
		Collection<Metadata> col = regHandleMap.duplicates(new Handle(regHandle));
		if(col == null || col.size() == 0) return false;
		return true;
	}

	void clearCachedKey() {
		cachedKey.selfDestruct();
	}

	/**
	 * This method looks like it puts the Reg handle as key, but that is not the case. The reg handle is
	 * in essence a foreign key, the actual database primary key is the CryptoKey's handle. 
	 */
	@Override
	public void put(String regHandle, CryptoKey key) {
		KeyGenerationAlgorithm alg = key.getMetadata().getKeyAlgorithm();
		switch (alg) {
			case Symmetric: {
				SymmetricKeyContents skc = (SymmetricKeyContents) key;
				put(regHandle, skc);
				break;
			}
			case Curve25519: {
				Curve25519KeyContents skc = (Curve25519KeyContents) key;
				put(regHandle, skc);
				break;
			}
			case EC: {
				ECKeyContents skc = (ECKeyContents) key;
				put(regHandle, skc);
				break;
			}
			case RSA: {
				RSAKeyContents skc = (RSAKeyContents) key;
				put(regHandle, skc);
				break;
			}
			case NTRU: {
				NTRUKeyContents skc = (NTRUKeyContents) key;
				put(regHandle, skc);
				break;
			}
	
			default:
				throw new RuntimeException("Unknown KeyGenerationAlgorithm: " + alg);
		}
	}

	private void put(String regHandle, SymmetricKeyContents key) {
		Metadata metadata = new Metadata(key.getMetadata().getHandle());
		metadata.setKey(true);
		metadata.setRegistrationHandle(regHandle);
		metadata.setKeyGenerationAlgorithm(key.getMetadata().getKeyAlgorithm()
				.toString());
		metadata.setCreatedOn(key.getMetadata().getCreatedOn().getTime());
		SymmetricKeyContentsProtoBuilder builder = new SymmetricKeyContentsProtoBuilder(
				key);
		SymmetricKeyContentsProto proto = builder.build();
		putSecure(key.getMetadata().getHandle(), metadata, proto);
	}

	private void put(String regHandle, Curve25519KeyForPublication key) {
		Metadata metadata = new Metadata(key.getMetadata().getHandle());
		metadata.setKey(true);
		metadata.setRegistrationHandle(regHandle);
		metadata.setKeyGenerationAlgorithm(key.getMetadata().getKeyAlgorithm()
				.toString());
		metadata.setCreatedOn(key.getMetadata().getCreatedOn().getTime());
		if (key instanceof Signer) {
			C2KeyContentsProtoBuilder builder = new C2KeyContentsProtoBuilder(
					(Curve25519KeyContents) key);
			C2KeyContentsProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		} else {
			metadata.setForPublication(true);
			C2KeyForPublicationProtoBuilder builder = new C2KeyForPublicationProtoBuilder(
					key);
			C2KeyForPublicationProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		}
	}

	private void put(String regHandle, RSAKeyForPublication key) {
		Metadata metadata = new Metadata(key.getMetadata().getHandle());
		metadata.setKey(true);
		metadata.setRegistrationHandle(regHandle);
		metadata.setKeyGenerationAlgorithm(key.getMetadata().getKeyAlgorithm()
				.toString());
		metadata.setCreatedOn(key.getMetadata().getCreatedOn().getTime());
		metadata.setRSAKeySize(((RSAKeyMetadata)key.getMetadata()).strength);

		if (key instanceof Signer) {
			RSAKeyContentsProtoBuilder builder = new RSAKeyContentsProtoBuilder(
					(RSAKeyContents) key);
			RSAKeyContentsProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		} else {
			metadata.setForPublication(true);
			RSAKeyForPublicationProtoBuilder builder = new RSAKeyForPublicationProtoBuilder(
					key);
			RSAKeyForPublicationProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		}
	}

	private void put(String regHandle, ECKeyForPublication key) {
		Metadata metadata = new Metadata(key.getMetadata().getHandle());
		metadata.setKey(true);
		metadata.setRegistrationHandle(regHandle);
		metadata.setKeyGenerationAlgorithm(key.getMetadata().getKeyAlgorithm()
				.toString());
		metadata.setCreatedOn(key.getMetadata().getCreatedOn().getTime());
		if(key.usesNamedCurve()){
			metadata.setCurveName(key.curveName);
		}else{
			// TODO handle custom key
		}

		if (key instanceof Signer) {
			ECKeyContentsProtoBuilder builder = new ECKeyContentsProtoBuilder(
					(ECKeyContents) key);
			ECKeyContentsProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		} else {
			metadata.setForPublication(true);
			ECKeyForPublicationProtoBuilder builder = new ECKeyForPublicationProtoBuilder(
					key);
			ECKeyForPublicationProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		}
	}
	
	private void put(String regHandle, NTRUKeyForPublication key) {
		Metadata metadata = new Metadata(key.getMetadata().getHandle());
		metadata.setKey(true);
		metadata.setRegistrationHandle(regHandle);
		metadata.setKeyGenerationAlgorithm(key.getMetadata().getKeyAlgorithm()
				.toString());
		metadata.setCreatedOn(key.getMetadata().getCreatedOn().getTime());
		if(key.hasNamedParam()){
			metadata.setNTRUParamName(key.parameterEnum.toString());
		}else{
			// TODO handle custom key
		}

		if (key instanceof Signer) {
			NTRUKeyContentsProtoBuilder builder = new NTRUKeyContentsProtoBuilder(
					(NTRUKeyContents) key);
			NTRUKeyContentsProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		} else {
			metadata.setForPublication(true);
			NTRUKeyForPublicationProtoBuilder builder = new NTRUKeyForPublicationProtoBuilder(
					key);
			NTRUKeyForPublicationProto proto = builder.build();
			putSecure(key.getMetadata().getHandle(), metadata, proto);
		}
	}


	/* (non-Javadoc)
	 * @see com.cryptoregistry.client.storage.DatastoreViews#put(java.lang.String, com.cryptoregistry.CryptoContact)
	 */
	@Override
	public void put(String regHandle, CryptoContact contact) {
		Metadata metadata = new Metadata(contact.getHandle());
		metadata.setRegistrationHandle(regHandle);
		metadata.setContact(true);
		ContactProtoBuilder builder = new ContactProtoBuilder(contact);
		CryptoContactProto proto = builder.build();
		putSecure(contact.getHandle(), metadata, proto);
	}

	/* (non-Javadoc)
	 * @see com.cryptoregistry.client.storage.DatastoreViews#put(java.lang.String, com.cryptoregistry.signature.CryptoSignature)
	 */
	@Override
	public void put(String regHandle, CryptoSignature signature) {
		Metadata metadata = new Metadata(signature.getHandle());
		metadata.setRegistrationHandle(regHandle);
		metadata.setSignatureAlgorithm(signature.getSigAlg().toString());
		metadata.setCreatedOn(signature.metadata.createdOn.getTime());
		SignatureProtoBuilder builder = new SignatureProtoBuilder(signature);
		putSecure(signature.getHandle(), metadata, builder.build());
	}

	/* (non-Javadoc)
	 * @see com.cryptoregistry.client.storage.DatastoreViews#put(java.lang.String, com.cryptoregistry.MapData)
	 */
	@Override
	public void put(String regHandle, MapData local) {
		Metadata metadata = new Metadata(local.uuid);
		metadata.setRegistrationHandle(regHandle);
		metadata.setNamedMap(true);
		NamedMapProtoBuilder builder = new NamedMapProtoBuilder(local.uuid,
				local.data);
		putSecure(local.uuid, metadata, builder.build());
	}

	/* (non-Javadoc)
	 * @see com.cryptoregistry.client.storage.DatastoreViews#put(java.lang.String, com.cryptoregistry.ListData)
	 */
	@Override
	public void put(String regHandle, ListData remote) {
		Metadata metadata = new Metadata(remote.uuid);
		metadata.setRegistrationHandle(regHandle);
		metadata.setNamedList(true);
		NamedListProtoBuilder builder = new NamedListProtoBuilder(remote.uuid,
				remote.urls);
		putSecure(remote.uuid, metadata, builder.build());
	}

	protected void putSecure(String handle, Metadata meta, Message proto) {
		byte[] input = proto.toByteArray();
		Handle key = new Handle(handle);
		byte[] iv = new byte[16];
		rand.nextBytes(iv);
		AESCBCPKCS7 aes = new AESCBCPKCS7(cachedKey.getData(), iv);
		byte[] encrypted = aes.encrypt(input);
		String simpleName = proto.getClass().getSimpleName();
		SecureData value = new SecureData(encrypted, iv, simpleName);
		this.getSecureMap().put(key, value);
		this.getMetadataMap().put(key, meta);
	}
	
	/**
	 * Use for a single-match type query use-case such as a defined handle
	 * 
	 */
	public void get(SingleResultCriteria criteria) throws SuitableMatchFailedException {
			get(criteria.map,criteria.result);
	}
	
	/**
	 * Use for the scenario where we expect multiple results, e.g., all key materials registered under "Bob Smith"
	 * 
	 * @param criteria
	 */
	public void get(MultiResultCriteria criteria) {
		get(criteria.map,criteria.results);
}
	
	private void get(Map<MetadataTokens,Object> searchCriteria, SingleResult result) throws SuitableMatchFailedException{
		
		// short circuit if there is a handle because in our setup they cannot be duplicates anyway 
		if(searchCriteria.containsKey(MetadataTokens.handle)){
			String key = (String) searchCriteria.get(MetadataTokens.handle);
			SecureData data = this.getSecureMap().get(key);
			Metadata meta = this.getMetadataMap().get(key);
			try {
				result.setResult(StorageUtil.getSecure(cachedKey, data));
				result.setMetadata(meta);
				return;
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}
		
		Iterator<Handle> iter = this.getMetadataMap().keySet().iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata meta = this.getMetadataMap().get(h);
			if(meta.match(searchCriteria)){
				SecureData data = this.getSecureMap().get(h);
				try {
					result.setResult(StorageUtil.getSecure(cachedKey, data));
					result.setMetadata(meta);
					return;
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
		
		throw new SuitableMatchFailedException("No match found for "+searchCriteria);
	}
	
	private void get(Map<MetadataTokens,Object> searchCriteria, List<SingleResult> results) {
		
		// short circuit if there is a handle because in our setup they cannot be duplicates anyway 
		if(searchCriteria.containsKey(MetadataTokens.handle)){
			String key = (String) searchCriteria.get(MetadataTokens.handle);
			SecureData data = this.getSecureMap().get(new Handle(key));
			Metadata meta = this.getMetadataMap().get(new Handle(key));
			SingleResult result = new SingleResult();
			try {
				result.setResult(StorageUtil.getSecure(cachedKey, data));
				result.setMetadata(meta);
				results.add(result);
				return;
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}
		
		Iterator<Handle> iter = this.getMetadataMap().keySet().iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata meta = this.getMetadataMap().get(h);
			SingleResult result = new SingleResult();
			if(meta.match(searchCriteria)){
				SecureData data = this.getSecureMap().get(h);
				try {
					result.setResult(StorageUtil.getSecure(cachedKey, data));
					result.setMetadata(meta);
					results.add(result);
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * Get exact matching handle, throw an exception if not found
	 * 
	 */
	public void get(String handle, SingleResult result) throws SuitableMatchFailedException{
		Metadata meta = this.getMetadataMap().get(new Handle(handle));
		SecureData data = this.getSecureMap().get(new Handle(handle));
		
		if(meta == null || data == null) throw new SuitableMatchFailedException("No match found for "+handle);
		
		try {
			result.setResult(StorageUtil.getSecure(cachedKey, data));
		} catch (InvalidProtocolBufferException e) {
			throw new RuntimeException("Problem decrypting "+handle);
		}
		result.setMetadata(meta);
		return;
	}

	@Override
	public String getDbStatus() {
		EnvironmentStats status = this.db.getEnvironment().getStats(null);
		return status.toStringVerbose();
	}
	
}
