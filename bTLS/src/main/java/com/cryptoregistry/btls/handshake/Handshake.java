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

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.btls.handshake.init.Autoloader;
import com.cryptoregistry.btls.handshake.kem.BaseKEM;
import com.cryptoregistry.btls.handshake.validator.KeyValidator;
import com.cryptoregistry.client.security.Datastore;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.sleepycat.je.DatabaseException;

/**
 * Classes that encapsulate handshake logic should extend this container
 * 
 * @author Dave
 *
 */
public abstract class Handshake {
	
	protected HandshakeProtocol hp;

	protected InputStream in;
	protected OutputStream out;
	protected Datastore ds; // location of our key cache
	protected Autoloader autoloader;
	protected BaseKEM kem; // asymmetric key exchanger module
	protected KeyValidator validator; // key validation, for example for checking third party signatures
	
	protected boolean server; // our role, true if server, else false for client
	
	protected SymmetricKeyContents sharedSecretKey; // may or may not be set
	protected SymmetricKeyContents ephemeralSecretKey; // may or may not be set; if set, it is used
	
	public abstract void doHandshake() throws HandshakeFailedException;
	

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
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
		return validator;
	}

	public void setValidator(KeyValidator validator) {
		this.validator = validator;
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
			int count = in.read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}
	
	public byte readByte()throws IOException {
		int ch1 = in.read();
		return (byte) ch1;
	}
	
	public int readShort16() throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return ((ch1 << 8) + (ch2 << 0));
	}

	public int readInt32() throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	public void writeByte(byte b) throws IOException {
        out.write((b >>>  0) & 0xFF);
        out.flush();
    }
	
	public void writeShort(int v) throws IOException {
		short s = (short) v;
        out.write((s >>>  8) & 0xFF);
        out.write((s >>>  0) & 0xFF);
        out.flush();
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

}
