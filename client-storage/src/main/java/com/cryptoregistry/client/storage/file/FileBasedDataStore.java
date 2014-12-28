package com.cryptoregistry.client.storage.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoKeyMetadata;
import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.Signer;
import com.cryptoregistry.ec.*;
import com.cryptoregistry.client.security.DataStore;
import com.cryptoregistry.client.security.KeyManager;
import com.cryptoregistry.client.security.SuitableMatchFailedException;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.formats.JSONReader;

/**
 * The idea is to have folders for secured, unsecured, and published keys and to use the canonical form. Simple.
 * 
 * @author Dave
 *
 */
public class FileBasedDataStore implements DataStore {

	final KeyManager keyManager;
	final File securedKeysFolder;
	final File unsecuredKeysFolder;
	final File publishedKeysFolder;
	
	final Lock lock = new ReentrantLock();

	public FileBasedDataStore(KeyManager keyManager) {
		super();
		this.keyManager = keyManager;
		String path = keyManager.getDatastoreFolder();
		File rootPath = new File(path);
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}
		securedKeysFolder = new File(rootPath, "secured");
		unsecuredKeysFolder = new File(rootPath, "unsecured");
		publishedKeysFolder = new File(rootPath, "published");

	}

	public CryptoKey findSecuredKey(String regHandle, String keyHandle) {
		lock.lock();
		try {
			List<String> files = new ArrayList<String>();
			getFileNames(files, securedKeysFolder.toPath());
			for (String p : files) {
				if (present(p, regHandle, keyHandle)) {
					File f = new File(p);
					try (
							FileInputStream fin = new FileInputStream(f);
							InputStreamReader reader = new InputStreamReader(fin);
					){
						JSONReader jsonReader = new JSONReader(reader);
						KeyMaterials km = jsonReader.parse();
						for(CryptoKeyWrapper wrapper: km.keys()) {
							if(wrapper.isSecure()) {
								wrapper.unlock(keyManager.getPassword());
								if(keyHandle.indexOf(wrapper.getMetadata().getHandle())==0){
									return (CryptoKey) wrapper.getKeyContents();
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}finally{
			lock.unlock();
		}

		return null;
	}
	
	public CryptoKey findUnsecuredKey(String regHandle, String keyHandle) {
		lock.lock();
		try {
			List<String> files = new ArrayList<String>();
			getFileNames(files, unsecuredKeysFolder.toPath());
			for (String p : files) {
				if (present(p, regHandle, keyHandle)) {
					File f = new File(p);
					try (
							FileInputStream fin = new FileInputStream(f);
							InputStreamReader reader = new InputStreamReader(fin);
					){
						JSONReader jsonReader = new JSONReader(reader);
						KeyMaterials km = jsonReader.parse();
						for(CryptoKeyWrapper wrapper: km.keys()) {
							if(keyHandle.indexOf(wrapper.getMetadata().getHandle())==0){
								return (CryptoKey) wrapper.getKeyContents();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}finally{
			lock.unlock();
		}

		return null;

	}

	public CryptoKey findPublishedKey(String regHandle, String keyHandle) {
		lock.lock();
		try {
			List<String> files = new ArrayList<String>();
			getFileNames(files, publishedKeysFolder.toPath());
			for (String p : files) {
				if (present(p, regHandle, keyHandle)) {
					File f = new File(p);
					try (
							FileInputStream fin = new FileInputStream(f);
							InputStreamReader reader = new InputStreamReader(fin);
					){
						JSONReader jsonReader = new JSONReader(reader);
						KeyMaterials km = jsonReader.parse();
						for(CryptoKeyWrapper wrapper: km.keys()) {
							if(keyHandle.indexOf(wrapper.getMetadata().getHandle())==0){
								return (CryptoKey) wrapper.getKeyContents();
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}finally{
			lock.unlock();
		}
		return null;
	}

	private List<String> getFileNames(List<String> fileNames, Path dir) {
		lock.lock();
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
			for (Path path : stream) {
				if (path.toFile().isDirectory())
					getFileNames(fileNames, path);
				else {
					fileNames.add(path.toAbsolutePath().toString());
					//System.out.println(path.getFileName());
				}
			}
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		return fileNames;
	}

	private boolean present(String path, String... slist) {

		boolean ok = false;
		lock.lock();
		try {
			File aFile = new File(path);
			long length = aFile.length();

			try (
					FileInputStream in = new FileInputStream(aFile);
					FileChannel inChannel = in.getChannel();
				) {
				ByteBuffer buf = ByteBuffer.allocate((int) length);					
				inChannel.read(buf);
				buf.flip();
				ArrayList<byte[]> list = new ArrayList<byte[]>();
				for(String s: slist) {
					list.add(s.getBytes(StandardCharsets.UTF_8));
				}
				ok = new KMPMatch().allPresent(buf.array(), list);
				buf.clear();
			}
		} catch (IOException x) {}
		finally {
			lock.unlock();
		}

		return ok;
	}
	
	
	
	public void savePublishedKey(String regHandle, CryptoKey key){
		lock.lock();
		try {
			File f = new File(publishedKeysFolder, key.getMetadata().getHandle());
			JSONFormatter formatter = new JSONFormatter(regHandle);
			formatter.add(key);
			try (
				FileOutputStream fout = new FileOutputStream(f);
				OutputStreamWriter out = new OutputStreamWriter(fout);
			) {
				formatter.format(out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}finally{
			lock.unlock();
		}
	}

	/**
	 * Return null if not found
	 * 
	 */
	@Override
	public CryptoKey findKey(String regHandle, String keyHandle) {
		CryptoKey key = findSecuredKey(regHandle,keyHandle);
		if(key == null){
			key = findUnsecuredKey(regHandle,keyHandle);
			if(key == null) {
				key = findPublishedKey(regHandle,keyHandle);
			}
		}
		return key;
	}

	@Override
	public void saveKey(String regHandle, CryptoKey key) {
		if(key instanceof Signer) {
			// do nothing - this interface should not be used to save secured keys
		}else{
			this.savePublishedKey(regHandle, key);
		}
	}
	

	@Override
	public CryptoKey preferredHandshakeKey(CryptoKey remoteKey, boolean generate)
			throws SuitableMatchFailedException {
		
		CryptoKeyMetadata meta = remoteKey.getMetadata();
		switch(meta.getKeyAlgorithm()) {
			case Symmetric: {
				// do nothing
				break;
			}
			case Curve25519: {
				
				break;
			}
			case EC: {
				ECKeyForPublication pk = (ECKeyForPublication)remoteKey;
				
				// iterate over all keys in the unsecured folder looking for an EC key to match the params
				List<String> files = new ArrayList<String>();
				getFileNames(files, securedKeysFolder.toPath());
				for (String p : files) {
				
					if(pk.usesNamedCurve()){
						if(present(p, pk.metadata.getKeyAlgorithm().toString(), pk.curveName)){
							// looks like a match, load key and compare curve for equality as a final check
						}
					}else{
						// for a custom curve there is some difficulty matching, so generate if allowed
						if(generate){
							return createAWorkingECKey(pk);
						}else{
							throw new SuitableMatchFailedException("custom curve mathcing doesn't work yet, and generate is false");
						}
					}
				}
				
				break;
			}
			default: throw new RuntimeException("Unknown KeyGenerationAlgorithm: "+meta.getKeyAlgorithm());
		}
		
		return null;
	}

	private ECKeyContents createAWorkingECKey(ECKeyForPublication contents){
		
		return null;
	}

}
