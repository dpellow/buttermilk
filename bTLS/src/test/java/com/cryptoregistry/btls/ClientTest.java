package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.cryptoregistry.c2.key.Curve25519KeyContents;

public class ClientTest {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.ClientTest");
	
	public static void main(String[] args) {
		
		C2Socket socket=null;
		Curve25519KeyContents clientKey = Configuration.CONFIG.clientKey();
	
		try {
			socket = new C2Socket(clientKey, "127.0.0.1", 4444);
			log.info("Client socket: "+socket);
			boolean OK = socket.clientsHandshake();
			log.info("Handshake status: "+OK);
			if(OK){
				SecureMessageOutputStream out = (SecureMessageOutputStream) socket.getOutputStream();
				out.submit("This is a test - Hi There!");
				out.flush();
				log.info("Sent message...");
				  try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				log.error("Failed handshake");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket != null){
				log.info("Cleaning up...");
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.exit(0);

	}

}
