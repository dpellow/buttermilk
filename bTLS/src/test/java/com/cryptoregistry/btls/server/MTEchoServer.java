package com.cryptoregistry.btls.server;

import java.net.*;
import java.io.*;

import com.cryptoregistry.btls.SecureServerSocket;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;

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
}