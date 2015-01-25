/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake.init;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.btls.handshake.Handshake;
import com.cryptoregistry.btls.handshake.HandshakeFailedException;
import com.cryptoregistry.btls.handshake.HandshakeProtocol;
import com.cryptoregistry.btls.handshake.kem.BasicC2KeyExchangeModule;
import com.cryptoregistry.btls.handshake.validator.digest.Sha256DigestValidator;
import com.cryptoregistry.btls.handshake.validator.key.PassthroughKeyValidator;

/**
 * When the handshake request comes in, load (initialize) the required sub-components to do that kind of handshake.
 * This is sort of a lazy load pattern based on the requested handshake
 * 
 * @author Dave
 *
 */
public class BasicAutoloader implements Autoloader {

	Handshake handshake;
	Set<AutoloadListener> listeners;

	public BasicAutoloader(Handshake handshake) {
		super();
		this.handshake = handshake;
		listeners = new HashSet<AutoloadListener>();
	}
	
	/**
	 * The autoloader is going to read the first frame from the input stream, it will be
	 * 
	 * 1 byte = "b" for buttermilk
	 * 1 byte = contentType (Handshake)
	 * 1 short = Handshake protocol code
	 */
	public void load() throws HandshakeFailedException {
		
		if(handshake.isServer()){
			//server mode - get proposed protocol from client
			int code = -1;
			
			// blocks until client sends it
			try {
				char ch = (char) handshake.readByte();
				if(ch == 'b') System.err.println("Started bTLS...");
				code = handshake.readByte();

				switch(code){
					case BTLSProtocol.HANDSHAKE: {
						int protocolCode = handshake.readShort16();
						for(HandshakeProtocol hp :HandshakeProtocol.values()) {
							if(hp.handshakeCode == protocolCode) {
								handshake.setHp(hp);
								_load(hp);
							}
						}
						break;
					}
					default: throw new HandshakeFailedException("Got the wrong code from from readByte: "+code);
				}
				
			}catch(IOException x){
				x.printStackTrace();
			}
			
		}else{
			// client mode
			_load(handshake.getHp());
		}
	}
	
	/**
	 * We've read the handshake protocol code, setting up now for a specific protocol's subcomponents
	 * 
	 * @param hp
	 * @throws HandshakeFailedException
	 */
	void _load(HandshakeProtocol hp) throws HandshakeFailedException {
		
		switch(hp){
			case H0:
			case H1: {
				
				break;
			}
			case H2: { // C2 Ephemeral keys
				handshake.setKem(new BasicC2KeyExchangeModule(handshake));
				handshake.setKeyValidator(new PassthroughKeyValidator());
				handshake.setDigestValidator(new Sha256DigestValidator(handshake));
				break;
			}
			
			default: throw new HandshakeFailedException("Unknown Protocol: "+hp.toString());
		}
		
		for(AutoloadListener al: listeners){
			al.autoloadCompleted(new AutoloadEvent(this,handshake));
		}
		
	}
	
	public void addAutoloadListener(AutoloadListener listener){
		this.listeners.add(listener);
	}

}
