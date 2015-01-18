package com.cryptoregistry.btls.server;

import java.net.*;
import java.io.*;

import com.cryptoregistry.btls.SecureServerSocket;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;

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

	public static void main(String[] args) {
		try {
			new EchoServer().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}