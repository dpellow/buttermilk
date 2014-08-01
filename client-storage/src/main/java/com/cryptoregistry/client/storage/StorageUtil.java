package com.cryptoregistry.client.storage;

import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.proto.reader.C2KeyContentsProtoReader;
import com.cryptoregistry.proto.reader.CryptoContactProtoReader;
import com.cryptoregistry.proto.reader.RSAKeyContentsProtoReader;
import com.cryptoregistry.protos.Buttermilk.C2KeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.CryptoContactProto;
import com.cryptoregistry.protos.Buttermilk.ECKeyContentsProto;
import com.cryptoregistry.protos.Buttermilk.RSAKeyContentsProto;
import com.cryptoregistry.symmetric.AESGCM;
import com.google.protobuf.InvalidProtocolBufferException;

public class StorageUtil {

	private static ReentrantLock lock0 = new ReentrantLock();

	public static Object getSecure(SensitiveBytes cachedKey, SecureData data) throws InvalidProtocolBufferException{

		lock0.lock();
		try {

			String protoName = data.getProtoClass();
			AESGCM gcm = new AESGCM(cachedKey.getData(), data.getIv());
			byte[] bytes = gcm.decrypt(data.getData());
			switch (protoName) {
			
			case "C2KeyContentsProto": {
				C2KeyContentsProto proto = C2KeyContentsProto.parseFrom(bytes);
				C2KeyContentsProtoReader reader = new C2KeyContentsProtoReader(proto);
				return reader.read();
			}
			case "CryptoContactProto": {
				CryptoContactProto proto  = CryptoContactProto.parseFrom(bytes);
				CryptoContactProtoReader reader = new CryptoContactProtoReader(proto);
				return reader.read();
			}
			case "ECKeyContentsProto": {
				ECKeyContentsProto proto = ECKeyContentsProto.parseFrom(bytes);
				
				break;
			}
			case "NamedMapProto": {
				
				break;
			}
			/**
			case "NTRUKeyContentsProto": {
				return NamedListProto.parseFrom(bytes);
			}
			*/
			case "RSAKeyContentsProto": {
				RSAKeyContentsProto proto = RSAKeyContentsProto.parseFrom(bytes);
				RSAKeyContentsProtoReader reader = new RSAKeyContentsProtoReader(proto);
				return reader.read();
			}
			case "SignatureProto": {
				//return SignatureProto.parseFrom(bytes);
				break;
			}
			default:
				throw new RuntimeException("Unknown proto: " + protoName);
			}
		} finally {
			lock0.unlock();
		}
		
		return null;
	}

}
