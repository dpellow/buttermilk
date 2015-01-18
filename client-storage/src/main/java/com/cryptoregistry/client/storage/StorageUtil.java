/*
 *  This file is part of Buttermilk(TM) 
 *  Copyright 2015 David R. Smith for cryptoregistry.com
 *
 */
package com.cryptoregistry.client.storage;

import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.reader.C2KeyContentsProtoReader;
import com.cryptoregistry.proto.reader.C2KeyForPublicationProtoReader;
import com.cryptoregistry.proto.reader.CryptoContactProtoReader;
import com.cryptoregistry.proto.reader.ECKeyContentsProtoReader;
import com.cryptoregistry.proto.reader.ECKeyForPublicationProtoReader;
import com.cryptoregistry.proto.reader.NamedListProtoReader;
import com.cryptoregistry.proto.reader.NamedMapProtoReader;
import com.cryptoregistry.proto.reader.RSAKeyContentsProtoReader;
import com.cryptoregistry.proto.reader.RSAKeyForPublicationProtoReader;
import com.cryptoregistry.proto.reader.SignatureProtoReader;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.C2KeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.NamedListProto;
import com.cryptoregistry.protos.Buttermilk.NamedMapProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyForPublicationProto;
import com.cryptoregistry.protos.Buttermilk.SignatureProto;
import com.cryptoregistry.symmetric.AESCBCPKCS7;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Helper class for dealing with encryption
 * 
 * @author Dave
 *
 */
public class StorageUtil {

	private static ReentrantLock lock0 = new ReentrantLock();

	public static Object getSecure(SensitiveBytes cachedKey, SecureData data) throws InvalidProtocolBufferException{

		lock0.lock();
		try {

			String protoName = data.getProtoClass();
			AESCBCPKCS7 aes = new AESCBCPKCS7(cachedKey.getData(), data.getIv());
			byte[] bytes = aes.decrypt(data.getData());
			switch (protoName) {
			
			case "C2KeyContentsProto": {
				C2KeyContentsProto proto = C2KeyContentsProto.parseFrom(bytes);
				C2KeyContentsProtoReader reader = new C2KeyContentsProtoReader(proto);
				return reader.read();
			}
			case "C2KeyForPublicationProto": {
				C2KeyForPublicationProto proto = C2KeyForPublicationProto.parseFrom(bytes);
				C2KeyForPublicationProtoReader reader = new C2KeyForPublicationProtoReader(proto);
				return reader.read();
			}
			case "CryptoContactProto": {
				CryptoContactProto proto  = CryptoContactProto.parseFrom(bytes);
				CryptoContactProtoReader reader = new CryptoContactProtoReader(proto);
				return reader.read();
			}
			case "ECKeyContentsProto": {
				ECKeyContentsProto proto = ECKeyContentsProto.parseFrom(bytes);
				ECKeyContentsProtoReader reader = new ECKeyContentsProtoReader(proto);
				return reader.read();
			}
			case "ECKeyForPublicationProto": {
				ECKeyForPublicationProto proto = ECKeyForPublicationProto.parseFrom(bytes);
				ECKeyForPublicationProtoReader reader = new ECKeyForPublicationProtoReader(proto);
				return reader.read();
			}
			case "NamedMapProto": {
				NamedMapProto proto = NamedMapProto.parseFrom(bytes);
				NamedMapProtoReader reader = new NamedMapProtoReader(proto);
				return reader.read();
			}
			case "NamedListProto": {
				NamedListProto proto = NamedListProto.parseFrom(bytes);
				NamedListProtoReader reader = new NamedListProtoReader(proto);
				return reader.read();
			}
			// TODO
			/**
			case "NTRUKeyContentsProto": {
				return NTRUKeyContentsProto.parseFrom(bytes);
			}
			*/
			/**
			case "NTRUKeyForPublicationProto": {
				return NTRUKeyForPublicationProto.parseFrom(bytes);
			}
			*/
			case "RSAKeyContentsProto": {
				RSAKeyContentsProto proto = RSAKeyContentsProto.parseFrom(bytes);
				RSAKeyContentsProtoReader reader = new RSAKeyContentsProtoReader(proto);
				return reader.read();
			}
			case "RSAKeyForPublicationProto": {
				RSAKeyForPublicationProto proto = RSAKeyForPublicationProto.parseFrom(bytes);
				RSAKeyForPublicationProtoReader reader = new RSAKeyForPublicationProtoReader(proto);
				return reader.read();
			}
			case "SignatureProto": {
				SignatureProto proto = SignatureProto.parseFrom(bytes);
				SignatureProtoReader reader = new SignatureProtoReader(proto);
				return reader.read();
			}
			default:
				throw new RuntimeException("Unknown proto: " + protoName);
			}
		} finally {
			lock0.unlock();
		}
	}
}
