package com.cryptoregistry.btls.client;

import java.io.*;
import java.net.*;

import com.cryptoregistry.btls.ClientSocketSecureConnector;
import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.btls.handshake.HandshakeProtocol;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;

public class EchoClient {

	String clientDbPath = "C:/Users/Dave/workspace-cryptoregistry/buttermilk/client-storage/data";
	Datastore ds = null;

	public void init() throws IOException {

		String hostName = "localhost";
		int portNumber = 4444;
		SimpleKeyManager km = new SimpleKeyManager(clientDbPath);
		ds = new BDBDatastore(km);

		Socket eSocket = null;
		ClientSocketSecureConnector connector = new ClientSocketSecureConnector(
				HandshakeProtocol.H2, 
				ds, 
				new Socket(hostName, portNumber)
		);

		try {
			eSocket = connector.connectSecure();
		} catch (HandshakeFailedException e1) {
			e1.printStackTrace();
		}

		try (
			PrintWriter out = new PrintWriter(eSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(eSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "+ hostName);
		}finally{
			if(ds != null) {
				ds.close();
			}
		}
	}
	
	public static void main(String [] args){
		try {
			new EchoClient().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
