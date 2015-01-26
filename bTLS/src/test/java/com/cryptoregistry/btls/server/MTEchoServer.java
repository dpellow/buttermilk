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
 * Naive multi-threaded echo server
 * 
 * @author Dave
 *
 */
public class MTEchoServer {

	SecureServerSocket serverSocket;
	String serverDbPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data2";
	Datastore ds;
	int portNumber = 4444;

	public MTEchoServer() {
		SimpleKeyManager km = new SimpleKeyManager(serverDbPath);
		ds = new BDBDatastore(km);
		keyMaintenance();
	}

	public void close() throws IOException {
		ds.close();
		serverSocket.close();
	}

	public void connect() throws IOException {
		serverSocket = new SecureServerSocket(ds, 4444);
	}

	public boolean accept() throws IOException {
		if (serverSocket.isClosed())
			return false;
		try {
			Socket socket = serverSocket.accept();
			Thread t = new Thread(new EchoServerWorker(socket));
			t.start();
		} catch (Exception x) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {

		MTEchoServer server = null;

		try {

			server = new MTEchoServer();
			server.connect();
			boolean ok = true;
			while (ok) {
				ok = server.accept();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (server != null)
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	private void keyMaintenance(){
		
		System.err.println("Starting key maintenance...");
		BDBDatastore bdb = (BDBDatastore) ds;
		System.err.println("cleaning keystore...");
		bdb.cleanOut();
		
		System.err.println("Building C2 key...");
		Curve25519KeyContents c1 = com.cryptoregistry.c2.CryptoFactory.INSTANCE.generateKeys();
		ds.getViews().put(ds.getRegHandle(), c1);
		
		System.err.println("Building EC key...");
		ECKeyContents c2 = com.cryptoregistry.ec.CryptoFactory.INSTANCE.generateKeys("P-256");
		ds.getViews().put(ds.getRegHandle(), c2);
		
		System.err.println("Building RSA key...");
		RSAKeyContents r2 = com.cryptoregistry.rsa.CryptoFactory.INSTANCE.generateKeys();
		ds.getViews().put(ds.getRegHandle(), r2);
		
		System.err.println("Keys:");
		Set<Handle> keys = ds.getViews().getMetadataMap().keySet();
		Iterator<Handle> iter = keys.iterator();
		while(iter.hasNext()){
			Handle h = iter.next();
			Metadata m = ds.getViews().getMetadataMap().get(h);
			System.err.println(h.getHandle()+", "+m.toString());
		}
	}
}