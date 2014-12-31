/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2013 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
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

public class ButtermilkViews {

	private final StoredSortedMap<Handle, SecureData> secureMap;
	private final StoredSortedMap<Handle, Metadata> metadataMap;
	private final SecureRandom rand = new SecureRandom();
	private final SensitiveBytes cachedKey;

	/**
	 * Create the data bindings and collection views.
	 */

	public ButtermilkViews(ButtermilkDatabase db, SensitiveBytes cachedKey) {

		this.cachedKey = cachedKey;
		ClassCatalog catalog = db.getClassCatalog();

		EntryBinding<Handle> secureKeyBinding = new SerialBinding<Handle>(
				catalog, Handle.class);
		EntryBinding<SecureData> secureDataBinding = new SerialBinding<SecureData>(
				catalog, SecureData.class);

		EntryBinding<Handle> metadataKeyBinding = new SerialBinding<Handle>(
				catalog, Handle.class);
		EntryBinding<Metadata> metadataDataBinding = new SerialBinding<Metadata>(
				catalog, Metadata.class);

		secureMap = new StoredSortedMap<Handle, SecureData>(
				db.getSecureDatabase(), secureKeyBinding, secureDataBinding,
				true);

		metadataMap = new StoredSortedMap<Handle, Metadata>(
				db.getMetadataDatabase(), metadataKeyBinding,
				metadataDataBinding, true);

	}

	public StoredSortedMap<Handle, SecureData> getSecureMap() {
		return secureMap;
	}

	public StoredSortedMap<Handle, Metadata> getMetadataMap() {
		return metadataMap;
	}

	void clearCachedKey() {
		cachedKey.selfDestruct();
	}

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

	public void put(String regHandle, SymmetricKeyContents key) {
		Metadata metadata = new Metadata();
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

	public void put(String regHandle, Curve25519KeyForPublication key) {
		Metadata metadata = new Metadata();
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

	public void put(String regHandle, RSAKeyForPublication key) {
		Metadata metadata = new Metadata();
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

	public void put(String regHandle, ECKeyForPublication key) {
		Metadata metadata = new Metadata();
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
	
	public void put(String regHandle, NTRUKeyForPublication key) {
		Metadata metadata = new Metadata();
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


	public void put(String regHandle, CryptoContact contact) {
		Metadata metadata = new Metadata();
		metadata.setRegistrationHandle(regHandle);
		metadata.setContact(true);
		ContactProtoBuilder builder = new ContactProtoBuilder(contact);
		CryptoContactProto proto = builder.build();
		putSecure(contact.getHandle(), metadata, proto);
	}

	public void put(String regHandle, CryptoSignature signature) {
		Metadata metadata = new Metadata();
		metadata.setRegistrationHandle(regHandle);
		metadata.setSignatureAlgorithm(signature.getSigAlg().toString());
		metadata.setCreatedOn(signature.metadata.createdOn.getTime());
		SignatureProtoBuilder builder = new SignatureProtoBuilder(signature);
		putSecure(signature.getHandle(), metadata, builder.build());
	}

	public void put(String regHandle, MapData local) {
		Metadata metadata = new Metadata();
		metadata.setRegistrationHandle(regHandle);
		metadata.setNamedMap(true);
		NamedMapProtoBuilder builder = new NamedMapProtoBuilder(local.uuid,
				local.data);
		putSecure(local.uuid, metadata, builder.build());
	}

	public void put(String regHandle, ListData remote) {
		Metadata metadata = new Metadata();
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

	public Object getSecure(String handle)
			throws InvalidProtocolBufferException {
		SecureData data = this.getSecureMap().get(new Handle(handle));
		return StorageUtil.getSecure(cachedKey, data);
	}
	
	/**
	 * Return the first matching object
	 * 
	 * @param searchCriteria
	 * @return
	 * @throws SuitableMatchFailedException 
	 */
	public Object getSecure(Map<MetadataTokens,Object> searchCriteria) throws SuitableMatchFailedException{
		
		Iterator<Handle> iter = this.getMetadataMap().keySet().iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata meta = this.getMetadataMap().get(h);
			if(meta.match(searchCriteria)){
				SecureData data = this.getSecureMap().get(h);
				try {
					return StorageUtil.getSecure(cachedKey, data);
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
		
		throw new SuitableMatchFailedException("No match found for "+searchCriteria);
	}
	
	public ECKeyContents getMostRecentECKey(String regHandle, String curveName) throws SuitableMatchFailedException {
		Map<MetadataTokens,Object> criteria = new HashMap<MetadataTokens,Object>();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "EC");
		criteria.put(MetadataTokens.curveName, curveName);
		
		List<Object> list = getMatchList(criteria);
		if(list.size() == 0) throw new SuitableMatchFailedException();
		if(list.size()==1) return (ECKeyContents) list.get(0);
		
		// more than one - find the date which is most recent
		ECKeyContents item = (ECKeyContents) list.get(0);
		Iterator<Object> iter = list.iterator();
		while(iter.hasNext()){
			ECKeyContents c = (ECKeyContents)iter.next();
			if(c.metadata.createdOn.after(item.metadata.createdOn)){
				item = c;
			}
		}

		return item;
	}
	
	public RSAKeyContents getMostRecentRSAKey(String regHandle, int size) throws SuitableMatchFailedException {
		Map<MetadataTokens,Object> criteria = new HashMap<MetadataTokens,Object>();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "RSA");
		criteria.put(MetadataTokens.RSAKeySize, size);
		
		List<Object> list = getMatchList(criteria);
		if(list.size() == 0) throw new SuitableMatchFailedException();
		if(list.size()==1) return (RSAKeyContents) list.get(0);
		
		// more than one - find the date which is most recent
		RSAKeyContents item = (RSAKeyContents) list.get(0);
		Iterator<Object> iter = list.iterator();
		while(iter.hasNext()){
			RSAKeyContents c = (RSAKeyContents)iter.next();
			if(c.metadata.createdOn.after(item.metadata.createdOn)){
				item = c;
			}
		}

		return item;
	}
	
	public Curve25519KeyContents getMostRecentC2Key(String regHandle) throws SuitableMatchFailedException {
		Map<MetadataTokens,Object> criteria = new HashMap<MetadataTokens,Object>();
		criteria.put(MetadataTokens.key, true);
		criteria.put(MetadataTokens.forPublication, false);
		criteria.put(MetadataTokens.keyGenerationAlgorithm, "Curve25519");
		
		List<Object> list = getMatchList(criteria);
		if(list.size() == 0) throw new SuitableMatchFailedException();
		if(list.size()==1) return (Curve25519KeyContents) list.get(0);
		
		// more than one - find the date which is most recent
		Curve25519KeyContents item = (Curve25519KeyContents) list.get(0);
		Iterator<Object> iter = list.iterator();
		while(iter.hasNext()){
			Curve25519KeyContents c = (Curve25519KeyContents)iter.next();
			if(c.metadata.createdOn.after(item.metadata.createdOn)){
				item = c;
			}
		}

		return item;
	}

	/**
	 * Return a (possibly empty) list of all matches
	 * 
	 * @param searchCriteria
	 * @return
	 */
	public List<Object> getMatchList(Map<MetadataTokens,Object> searchCriteria){
		
		List<Object> results = new ArrayList<Object>();
		Iterator<Handle> iter = this.getMetadataMap().keySet().iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata meta = this.getMetadataMap().get(h);
			if(meta.match(searchCriteria)){
				SecureData data = this.getSecureMap().get(h);
				try {
					results.add(StorageUtil.getSecure(cachedKey, data));
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}
			}
		}
		
		return results;
	}
}
