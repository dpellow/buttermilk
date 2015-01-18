package com.cryptoregistry.btls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.cryptoregistry.btls.handshake.BasicHandshake;
import com.cryptoregistry.btls.handshake.Handshake;
import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.btls.handshake.HandshakeProtocol;
import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;

public class BTLSSocketTest {
	
	/**
	 * Test the handshake module
	 * @throws IOException
	 */
	@Test
	public void test0() throws IOException {
		
		String clientDbPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data";
		String serverDbPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data2";

		PipedOutputStream clientOut = new PipedOutputStream();
		PipedOutputStream serverOut = new PipedOutputStream();
		PipedInputStream clientIn = new PipedInputStream(16);
		PipedInputStream serverIn = new PipedInputStream(16);
		// connect them
		clientOut.connect(serverIn);
		clientIn.connect(serverOut);
	
		new Thread(new Client0(HandshakeProtocol.H2, clientDbPath, clientIn, clientOut)).start();
		Client0 client0 =	new Client0(serverDbPath, serverIn, serverOut);
		client0.run();
	
		Assert.assertTrue(true);
	}
	
	
	class Client0 implements Runnable {
		
		Handshake handshake;
		Datastore ds = null;
		
		// for server
		public Client0(String path,  InputStream in, OutputStream out){
			SimpleKeyManager km = new SimpleKeyManager(path);
			ds = new BDBDatastore(km);
			handshake = new BasicHandshake(ds);
			handshake.setIn(in);
			handshake.setOut(out);
		}
		
		// for client
		public Client0(HandshakeProtocol hp, String path, InputStream in, OutputStream out){
			SimpleKeyManager km = new SimpleKeyManager(path);
			ds = new BDBDatastore(km);
			handshake = new BasicHandshake(hp, ds);
			handshake.setIn(in);
			handshake.setOut(out);
		}

		@Override
		public void run() {
			
			// clean the store from previous efforts
			BDBDatastore ds = (BDBDatastore)  handshake.getDs();
			ds.cleanOut();
			
			// create a key and store it
			Curve25519KeyContents c1 = CryptoFactory.INSTANCE.generateKeys();
			ds.getViews().put(ds.getDefaultRegHandle(), c1);
			
			try {
				handshake.doHandshake();
				System.err.println("Handshake "+handshake+" complete");
				
			} catch (HandshakeFailedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	@Test
	public void test1() throws IOException {
		
		String clientDbPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data";
		
		
	}

}
