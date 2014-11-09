package com.cryptoregistry.btls;

import x.org.bouncycastle.crypto.io.CipherInputStream;
import x.org.bouncycastle.crypto.io.CipherOutputStream;


/**
 * Handshake modules for various connection protocols use this interface
 * 
 * @author Dave
 *
 */
public interface Handshake {
	public boolean clientsHandshake();
	public boolean serversHandshake();
	public CipherOutputStream decorateOutputStream();
	public CipherInputStream decorateInputStream();
}