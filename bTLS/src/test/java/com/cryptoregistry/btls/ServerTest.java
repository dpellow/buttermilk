package com.cryptoregistry.btls;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.cryptoregistry.c2.key.Curve25519KeyContents;

/**
 * A simple test server, just print out anything sent to us
 * 
 * @author Dave
 *
 */
public class ServerTest {
	
	private static final Logger log = Logger.getLogger("com.cryptography.btls.ServerTest");
	final static int port = 4444;


	public static void main(String[] args) {
	
		log.info("Starting server on port "+port);
		SecureServerSocket ss = null;
		Curve25519KeyContents serverKey = Configuration.CONFIG.serverKey();
		log.info("ServerKey:"+serverKey.getDistinguishedHandle());
		try {
			ss = new SecureServerSocket(
					serverKey, 
					port, 
					50, 
					InetAddress.getByAddress(new byte[] {0x7f,0x00,0x00,0x01})
			);
			log.info("Created server socket. Going into loop...");
			while(true){
				// blocks until a connection is made.
				SecureSocket socket = (SecureSocket) ss.accept();
				// the PrintHandler prints one message and then exits by closing the socket
				Thread t = new Thread(new PrintHandler(socket));
				t.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(ss != null){
				log.info("Closing server socket, cleaning up.");
				try {
					if(!ss.isClosed()){
						ss.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
