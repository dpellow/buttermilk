package com.cryptoregistry.btls.io;

public class BTLSProtocol {

	// first byte
	public final static int ALERT = 10;
	public final static int APPLICATION = 11;
	public final static int HANDSHAKE = 12;
	
	// alert subcodes
	public final static int EMERGENCY_CLOSE_SOCKET_IMMEDIATELY = 1000; 
	public final static int INFORMATION = 1001; 
	
	
	// handshake subcodes
	// second and third bytes (as a short) (in the case of handshake as first byte)
	public final static int CLIENT_HELLO = 100; 
	public final static int  SEND_KEY = 101;
	public final static int  SERVER_HELLO = 102;
	public final static int CANNOT_SEND_KEY = 103;
	public final static int FAILED_VALIDATION = 104;
	public final static int FAILED_HANDSHAKE = 105;
	public final static int CLIENT_READY = 106;
	public final static int SERVER_READY = 107; 

}
