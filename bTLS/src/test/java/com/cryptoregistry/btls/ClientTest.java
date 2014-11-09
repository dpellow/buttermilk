package com.cryptoregistry.btls;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.proto.frame.StringOutputFrame;

public class ClientTest {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.ClientTest");
	
	public static void main(String[] args) {
		
		SecureSocket socket=null;
		Curve25519KeyContents clientKey = Configuration.CONFIG.clientKey();
		
		log.info("Client key: "+clientKey);
	
		try {
			socket = new SecureSocket("127.0.0.1", 4444);
			log.info("Client socket: "+socket);
			C2Handshake handshake = new C2Handshake(
					false, 
					clientKey, 
					socket.getRawInputStream(),
					socket.getRawOutputStream()
			);
			if(!handshake.clientsHandshake()){
				// failed, throw exception
				log.error("Failed handshake", new IOException("Handshake failed: "+socket.toString()));
			}else{
				// handshake also knows how to set up the Cipher stream decorators we will use
				socket.setStreams(handshake);
				BufferedOutputStream s = new BufferedOutputStream(socket.getOutputStream());
				StringOutputFrame of = new StringOutputFrame("Greetings, friends.");
				of.writeFrame(s);
				log.info("Sent message...");
				  try {
					Thread.sleep(1000);
				  } catch (InterruptedException e) {
					e.printStackTrace();
				  }
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket != null){
				log.info("Cleaning up...");
				try {
					if(!socket.isClosed()){
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.exit(0);

	}

}
