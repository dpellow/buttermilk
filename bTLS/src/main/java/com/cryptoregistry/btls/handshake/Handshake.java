/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.handshake;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.crypto.io.DigestInputStream;
import x.org.bouncycastle.crypto.io.DigestOutputStream;

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.btls.handshake.init.Autoloader;
import com.cryptoregistry.btls.handshake.kem.BaseKEM;
import com.cryptoregistry.btls.handshake.validator.digest.DigestValidator;
import com.cryptoregistry.btls.handshake.validator.key.KeyValidator;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.sleepycat.je.DatabaseException;

/**
 * Classes that encapsulate handshake logic should extend this container - normally they extend BasicHandshake
 * 
 * @author Dave
 *
 */
public abstract class Handshake {
	
	protected HandshakeProtocol hp;

	protected InputStream in;
	protected OutputStream out;
	
	protected DigestInputStream din;
	protected DigestOutputStream dout;
	
	protected Datastore ds; // location of our key cache
	protected Autoloader autoloader;
	protected BaseKEM kem; // asymmetric key exchanger module
	protected KeyValidator keyValidator; // key validation, for example for checking third party signatures
	protected DigestValidator manInTheMiddleCheck; // check the handshake is authentic
	
	protected boolean server; // our role, true if server, else false for client
	
	protected SymmetricKeyContents sharedSecretKey; // may or may not be set
	protected SymmetricKeyContents ephemeralSecretKey; // may or may not be set; if set, it is used
	
	public abstract void doHandshake() throws HandshakeFailedException;
	

	//public InputStream getIn() {
	//	return in;
	//}

	// also initializes the DigestInputStream
	public void setIn(InputStream in) {
		if(in != null) return;
		this.in = in;
		this.din = new DigestInputStream(in,new SHA256Digest());
	}

//	public OutputStream getOut() {
//		return out;
//	}

	// also initializes the DigestOutputStream
	public void setOut(OutputStream out) {
		if(out != null) return;
		this.out = out;
		this.dout = new DigestOutputStream(out, new SHA256Digest());
	}

	public Datastore getDs() {
		return ds;
	}

	public void setDs(Datastore ds) {
		this.ds = ds;
	}

	public BaseKEM getKem() {
		return kem;
	}

	public void setKem(BaseKEM kem) {
		this.kem = kem;
	}

	public KeyValidator getValidator() {
		return keyValidator;
	}

	public void setKeyValidator(KeyValidator validator) {
		this.keyValidator = validator;
	}

	public boolean isServer() {
		return server;
	}

	public void setServer(boolean server) {
		this.server = server;
	}
	
	public void readFully(byte b[], int off, int len)
			throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = din.read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}
	
	public byte readByte()throws IOException {
		int ch1 = din.read();
		return (byte) ch1;
	}
	
	public int readShort16() throws IOException {
		int ch1 = din.read();
		int ch2 = din.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return ((ch1 << 8) + (ch2 << 0));
	}

	public int readInt32() throws IOException {
		int ch1 = din.read();
		int ch2 = din.read();
		int ch3 = din.read();
		int ch4 = din.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	public void writeByte(byte b) throws IOException {
        dout.write((b >>>  0) & 0xFF);
        dout.flush();
    }
	
	public void writeShort(int v) throws IOException {
		short s = (short) v;
        dout.write((s >>>  8) & 0xFF);
        dout.write((s >>>  0) & 0xFF);
        dout.flush();
    }
	
	/**
	 * Called by client only.
	 * 
	 * byte 1 = 	"b" 
	 * byte 2 = 	BTLSProtocol.HANDSHAKE
	 * bytes 3-4 =  HandshakeProtocol (as a short)
	 * @param hp
	 * @throws IOException 
	 */
	public void startProtocol(){
		try {
			writeByte((byte)'b');
			writeByte((byte) BTLSProtocol.HANDSHAKE);
			writeShort(hp.handshakeCode);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void closeDs() throws DatabaseException {
		ds.close();
	}

	public String getDefaultRegHandle() {
		return ds.getRegHandle();
	}

	public HandshakeProtocol getHp() {
		return hp;
	}

	public void setHp(HandshakeProtocol hp) {
		this.hp = hp;
	}

	public Autoloader getAutoloader() {
		return autoloader;
	}

	public void setAutoloader(Autoloader autoloader) {
		this.autoloader = autoloader;
	}


	public DigestInputStream getDin() {
		return din;
	}

	public DigestOutputStream getDout() {
		return dout;
	}


	public void setManInTheMiddleCheck(DigestValidator manInTheMiddleCheck) {
		this.manInTheMiddleCheck = manInTheMiddleCheck;
	}
	
	

}
