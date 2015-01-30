/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls;

public class BTLSProtocol {

	// first byte
	public final static int ALERT = 10;
	public final static int APPLICATION = 11;
	public final static int HANDSHAKE = 12;
	public final static int HEARTBEAT = 14;
	
	// send and third bytes
	
	// alert subcodes
	public final static int EMERGENCY_CLOSE_SOCKET_IMMEDIATELY = 1000; 
	public final static int INFORMATION = 1001; 
	
	
	// handshake subcodes
	// second and third byte (as a short)
	public final static int CLIENT_HELLO = 100; 
	public final static int SEND_KEY = 101;
	public final static int SENDING_KEY = 102;
	public final static int SERVER_HELLO = 104;
	public final static int CANNOT_SEND_KEY = 105;
	public final static int FAILED_VALIDATION = 106;
	public final static int FAILED_HANDSHAKE = 107;
	public final static int SUCCESSFUL_VALIDATION = 108;
	public final static int KEY_RESOLVED = 103; 
	
	public final static int CLIENT_READY = 110;
	public final static int SERVER_READY = 111; 
	
	public final static int SEND_DIGEST = 112;
	public final static int SENDING_DIGEST = 114;
	

}
