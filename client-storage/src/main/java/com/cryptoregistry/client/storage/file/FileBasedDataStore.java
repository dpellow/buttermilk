package com.cryptoregistry.client.storage.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.CryptoKeyWrapper;
import com.cryptoregistry.KeyMaterials;
import com.cryptoregistry.client.security.KeyManager;
import com.cryptoregistry.formats.JSONReader;

/**
 * The idea is to have folders for secured, unsecured, and published keys and to use the canonical form. Simple.
 * 
 * @author Dave
 *
 */
public class FileBasedDataStore {

	KeyManager keyManager;
	File securedKeysFolder;
	File unsecuredKeysFolder;
	File publishedKeysFolder;

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

		return null;

	}
	
	public CryptoKey findUnsecuredKey(String regHandle, String keyHandle) {
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

		return null;

	}

	public CryptoKey findPublishedKey(String regHandle, String keyHandle) {
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
		return null;
	}

	private List<String> getFileNames(List<String> fileNames, Path dir) {
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
		}
		return fileNames;
	}

	private boolean present(String path, String regHandle, String keyHandle) {

		boolean ok = false;

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
				list.add(regHandle.getBytes("UTF-8"));
				list.add(keyHandle.getBytes("UTF-8"));
				ok = new KMPMatch().allPresent(buf.array(), list);
				buf.clear();
			}
		} catch (IOException x) {}

		return ok;
	}

}
