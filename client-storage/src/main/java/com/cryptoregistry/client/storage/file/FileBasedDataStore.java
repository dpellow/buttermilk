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
import com.cryptoregistry.ec.*;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.security.DatastoreViews;
import com.cryptoregistry.client.security.KeyManager;
import com.cryptoregistry.client.security.SuitableMatchFailedException;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.formats.JSONReader;
import com.sleepycat.je.DatabaseException;

/**
 * 
 * @author Dave
 *
 */
public class FileBasedDataStore implements Datastore {

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
	
	private void loadAll(){
		
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

	/**
	 * match in the file for an arbitrary number of tokens
	 * 
	 * @param path
	 * @param slist
	 * @return
	 */
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

	@Override
	public void close() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatastoreViews getViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultRegHandle() {
		// TODO Auto-generated method stub
		return null;
	}
	

	

}
