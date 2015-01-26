package com.cryptoregistry.btls.server;

import java.net.*;
import java.util.Iterator;
import java.util.Set;
import java.io.*;

import com.cryptoregistry.btls.SecureServerSocket;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.Handle;
import com.cryptoregistry.client.storage.Metadata;
import com.cryptoregistry.client.storage.SimpleKeyManager;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.rsa.RSAKeyContents;

/**
 * Naive echo server
 * 
 * @author Dave
 *
 */
public class EchoServer {

	String serverDbPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data2";
	Datastore ds = null;

	private void init() throws IOException {

		int portNumber = 4444;
		SimpleKeyManager km = new SimpleKeyManager(serverDbPath);
		ds = new BDBDatastore(km);
		keyMaintenance();

		InputStream in = null;

		try (SecureServerSocket serverSocket = new SecureServerSocket(ds, 4444);
				Socket socket = serverSocket.accept();

		) {
			in = socket.getInputStream();
			byte[] bytes = new byte[1024];
			while (true) {
				int count = in.read(bytes);
				String msg = new String(bytes, 0, count, "UTF-8");
				System.err.println("Got: " + msg);
				socket.getOutputStream().write(msg.getBytes());
				socket.getOutputStream().flush();
			}
		} catch (IOException e) {
			System.out
					.println("Exception caught when trying to listen on port "
							+ portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		} finally {
			if (ds != null) {
				ds.close();
			}
		}
	}
	
	private void keyMaintenance(){
		
		BDBDatastore bdb = (BDBDatastore) ds;
		bdb.cleanOut();
		
		Curve25519KeyContents c1 = com.cryptoregistry.c2.CryptoFactory.INSTANCE.generateKeys();
		ds.getViews().put(ds.getRegHandle(), c1);
		
		ECKeyContents c2 = com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys("P-256");
		ds.getViews().put(ds.getRegHandle(), c2);
		
		RSAKeyContents r2 = com.cryptoregistry.rsa.CryptoFactory.INSTANCE.generateKeys();
		ds.getViews().put(ds.getRegHandle(), r2);
		
		
		Set<Handle> keys = ds.getViews().getMetadataMap().keySet();
		Iterator<Handle> iter = keys.iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata m = ds.getViews().getMetadataMap().get(h);
			System.err.println(h.getHandle()+", "+m.toString());
		}
	}

	public static void main(String[] args) {
		try {
			new EchoServer().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}