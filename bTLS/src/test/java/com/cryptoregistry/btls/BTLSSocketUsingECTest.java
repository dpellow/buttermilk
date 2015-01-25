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
import com.cryptoregistry.btls.handshake.init.AutoloadEvent;
import com.cryptoregistry.btls.handshake.init.AutoloadListener;
import com.cryptoregistry.btls.handshake.init.BasicAutoloader;
import com.cryptoregistry.btls.handshake.kem.BaseKEM;
import com.cryptoregistry.btls.handshake.kem.KeyExchangeEvent;
import com.cryptoregistry.btls.handshake.kem.KeyExchangeListener;
import com.cryptoregistry.btls.handshake.validator.DigestValidationListener;
import com.cryptoregistry.btls.handshake.validator.KeyValidationListener;
import com.cryptoregistry.btls.handshake.validator.ValidationEvent;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;
import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;

public class BTLSSocketUsingECTest {
	
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
	
		new Thread(new Client0(HandshakeProtocol.H3, clientDbPath, clientIn, clientOut)).start();
		Client0 client0 =	new Client0(serverDbPath, serverIn, serverOut);
		client0.run();
	
		Assert.assertTrue(true);
	}
	
	
	class Client0 implements Runnable, AutoloadListener,
	KeyExchangeListener, KeyValidationListener, DigestValidationListener {
		
		Handshake handshake;
		Datastore ds = null;
		
		// for server
		public Client0(String path,  InputStream in, OutputStream out){
			SimpleKeyManager km = new SimpleKeyManager(path);
			ds = new BDBDatastore(km);
			handshake = new BasicHandshake(ds);
			handshake.setIn(in);
			handshake.setOut(out);
			BasicAutoloader autoloader = new BasicAutoloader(handshake);
			handshake.setAutoloader(autoloader);
			autoloader.addAutoloadListener(this);
		}
		
		// for client
		public Client0(HandshakeProtocol hp, String path, InputStream in, OutputStream out){
			SimpleKeyManager km = new SimpleKeyManager(path);
			ds = new BDBDatastore(km);
			handshake = new BasicHandshake(hp, ds);
			handshake.setIn(in);
			handshake.setOut(out);
			BasicAutoloader autoloader = new BasicAutoloader(handshake);
			handshake.setAutoloader(autoloader);
			autoloader.addAutoloadListener(this);
		}

		@Override
		public void run() {
			
			// clean the store from previous efforts
			BDBDatastore ds = (BDBDatastore)  handshake.getDs();
			ds.cleanOut();
			
			// create a key and store it
			ECKeyContents c1 = CryptoFactory.INSTANCE.generateKeys("P-256");
			ds.getViews().put(ds.getRegHandle(), c1);
			
			try {
				handshake.doHandshake();
				System.err.println("Handshake "+handshake+" complete");
				
			} catch (HandshakeFailedException e) {
				e.printStackTrace();
			}	
		}

		@Override
		public void digestComparisonCompleted(ValidationEvent evt) {
			System.err.println("digestComparisonCompleted: "+evt);
			
		}

		@Override
		public void keyValidationResult(ValidationEvent evt) {
			System.err.println("keyValidationResult: "+evt);
			
		}

		@Override
		public void secretExchangeCompleted(KeyExchangeEvent evt) {
			System.err.println("secretExchangeCompleted: "+evt);
			
		}

		@Override
		public void ephemeralExchangeCompleted(KeyExchangeEvent evt) {
			System.err.println("ephemeralExchangeCompleted: "+evt);
			
		}

		@Override
		public void autoloadCompleted(AutoloadEvent evt) {
			System.err.println("autoload completed, setting key exchange listener dynamically");
			BaseKEM mod = evt.getHandshake().getKem();
			mod.addKeyExchangeListener(this);
			evt.getHandshake().getKeyValidator().addKeyExchangeListener(this);
			evt.getHandshake().getDigestValidator()
					.addDigestValidationListener(this);
			
		}
	}
	

}
