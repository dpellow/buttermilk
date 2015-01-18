/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake;

/**
 * Handshakes use defined codes as a way to quickly communicate the desired protocol. For instance,
 * "Handshake #3" Uses ephemeral EC P-256 key exchange with a 256 bit shared secret. 
 * 
 * @author Dave
 *
 */
public enum HandshakeProtocol {
	
	H0(0, "Testing Only, uses pre-defined symmetric key"), // Control Handshake, for testing, uses a defined and published 256 bit key for the value
	
	H1(1, "Testing Only, uses pre-defined symmetric key"), // Control Handshake, for testing, Reserved code

	H2(2, "Simple Curve25519 Asymmetric Key exchange"),
	
	H3(3, "Elliptic Curve P-256 Asymmetric Key exchange"),
	
	H5(5, "A cryptoregistry.com custom key exchange using RSA"),
	
	H7(7, "TBA"), // Handshake #7 = NTRU, similar to #5
	
	H11(11, "TBA");
	
	public final int handshakeCode;
	public final String description;
	
	private HandshakeProtocol(int code, String desc){
		handshakeCode = code;
		description = desc;
	}

	public int getHandshakeCode() {
		return handshakeCode;
	}

	public String getDescription() {
		return description;
	}
	
}
