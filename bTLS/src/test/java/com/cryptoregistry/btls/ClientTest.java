package com.cryptoregistry.btls;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.proto.frame.StringOutputFrame;

/**
 * Works with ServerTest. Use a secure socket to send a String to the server and then exit.
 * 
 * @author Dave
 *
 */
public class ClientTest {

	private static final Logger log = Logger.getLogger("com.cryptography.btls.ClientTest");
	
	public static void main(String[] args) {
		
		// default message, else use from command line
		String msg = "Greetings";
		if(args.length > 0) msg = args[0];
		
		SecureSocket socket=null;
		Curve25519KeyContents clientKey = Configuration.CONFIG.clientKey();
		
		log.info("Client key: "+clientKey);
	
		try {
			socket = new SecureSocket("127.0.0.1", 4444);
			log.info("Client socket: "+socket);
			
			// first we configure our handshake
			C2Handshake handshake = new C2Handshake(
					false, // false because we are a client
					clientKey, // contains the private key for ECDH
					socket.getRawInputStream(), 
					socket.getRawOutputStream()
			);
			
			// run the handshake
			if(!handshake.clientsHandshake()){
				
				// failed, throw exception
				log.error("Failed handshake", new IOException("Handshake failed: "+socket.toString()));
			
			}else{
				
				// handshake was good
				// handshake also knows how to set up the CipherStream decorator classes we will use
				socket.setStreams(handshake);
				BufferedOutputStream s = new BufferedOutputStream(socket.getOutputStream());
				
				// from this point on communications are encrypted
				// the output frame uses a protocol buffer object prepended with the message length
				StringOutputFrame of = new StringOutputFrame(msg);
				of.writeFrame(s);
				log.info("Sent message...");
				
				// pause while it sends
				  try {
					Thread.sleep(100);
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
