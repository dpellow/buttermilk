/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.io;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.protos.Buttermilk.AuthenticatedStringProto;

import x.org.bouncycastle.crypto.Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.crypto.engines.AESFastEngine;
import x.org.bouncycastle.crypto.io.CipherInputStream;
import x.org.bouncycastle.crypto.macs.HMac;
import x.org.bouncycastle.crypto.modes.CBCBlockCipher;
import x.org.bouncycastle.crypto.paddings.PKCS7Padding;
import x.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import x.org.bouncycastle.crypto.params.KeyParameter;
import x.org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * FrameInputStream is a FilterInputStream with frame-reading capability. 
 * 
 * @author Dave
 *
 */
public class FrameInputStream extends FilterInputStream {
	
	static final Logger logger = LogManager.getLogger(FrameInputStream.class.getName());
	
	protected CipherInputStream cIn;
	protected byte [] key;
	protected ParametersWithIV params;
	protected KeyParameter hmacParam;
	protected Digest digest;
	PaddedBufferedBlockCipher aesCipher;
	
	protected Set<AlertListener> alertListeners;
	
	public FrameInputStream(InputStream in) {
		super(in);
		digest = new SHA256Digest();
		alertListeners = new HashSet<AlertListener>();
	}

	public FrameInputStream(InputStream in, byte [] key) {
		this(in);
		this.key = key;
	}
	
	public int read() throws IOException {
		if(cIn == null) readFrame();
		int val = cIn.read();
		if(val == -1) return readFrame();
		else return val;
	}
	
	public int read(byte[] in) throws IOException {
		if(cIn == null) readFrame();
			int count = cIn.read(in);
			if(count == -1){
				int code = readFrame();
				if(code == -1) return -1;
				else {
					return cIn.read(in);
				}
			}else{
				return count;
			}
	
	}

	public int read(byte[] in, int offset, int length) throws IOException {
		if(cIn == null) readFrame();
		int count = cIn.read(in,offset,length);
		if(count == -1){
			int code = readFrame();
			if(code == -1) return -1;
			else {
				return cIn.read(in);
			}
		}else{
			return count;
		}
	}
	
	public int readFrame(){
		
		logger.trace("Entering readFrame()");
		
		int code = -1;
		
		try {
			code = this.readByte(in);
			if(code == -1) return -1;

			switch(code){
			
				// alerts are not encrypted, but we do validate them using an HMac
				case BTLSProtocol.ALERT: {
					
					int alertCode = this.readShort16(in);
					int sz = this.readShort16(in);
					byte [] b = new byte[sz];
					this.readFully(in, b, 0, sz);
					AuthenticatedStringProto asp = AuthenticatedStringProto.parseFrom(b);
					String alertText = asp.getData();
					byte [] hmacBytes = asp.getHmac().toByteArray();
					hmacParam = new KeyParameter(key,0,key.length);
					
					if(!validateHMac(hmacParam, hmacBytes, alertText.getBytes("UTF-8"))) {
						throw new RuntimeException("Alert Frame appears to have been tampered with...bailing out.");
					}
					
					// consume the alert, then auto-advance to the next readFrame() 
					Iterator<AlertListener> iter = alertListeners.iterator();
					while(iter.hasNext()){
						AlertListener al = iter.next();
						al.alertReceived(new AlertEvent(this,alertCode,alertText));
					}
					return readFrame();
				}
				
				// application messages are encrypted and validated using an HMac
				case BTLSProtocol.APPLICATION: {
					
					logger.trace("Entering read on application frame...");
					
					// first there is a Byte String with the IV size and contents
					int IVSz = this.readShort16(in);
					byte [] iv = new byte[IVSz];
					this.readFully(in, iv, 0, IVSz);
					logger.trace("read IV: "+Arrays.toString(iv));
					
					// second there is the encrypted contents, max length is Integer.MAX_VALUE, about 2.15Gb
					int contentsSz = this.readInt32(in);
					byte [] contents = new byte[contentsSz];
					this.readFully(in, contents, 0, contentsSz);
					ByteArrayInputStream bin = new ByteArrayInputStream(contents);
					logger.trace("read encrypted contents: "+Arrays.toString(iv));
					
					// use the iv from the message along with the key to initialize the message
					params = buildKey(key,iv);
					logger.trace("built key: "+params);
					
					// setup for the CipherInputStream
					CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
					aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
					aesCipher.init(false, params);
					cIn = new CipherInputStream(bin,aesCipher);
					logger.trace("decrypting input stream set up: "+cIn);
					
					// third there is an hmac size and hmac bytes - application blocks are validated
					int macSz = this.readShort16(in);
					if(macSz == 0) break;// bail if no hmac provided
					hmacParam = new KeyParameter(key,0,key.length);
					byte [] hmacBytes = new byte[macSz];
					this.readFully(in, hmacBytes, 0, macSz);
					logger.trace("read hmac: "+Arrays.toString(hmacBytes));
					if(!validateHMac(hmacParam, hmacBytes, contents)) {
						logger.trace("validation of hmac failed...");
						throw new RuntimeException("Application Frame appears to have been tampered with...bailing out.");
					}else{
						logger.trace("validation of hmac successful!");
					}
					
					break;
					
					// cIn can now be read using the read() methods
					
				}
				default: throw new RuntimeException("unknown code: "+code);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}
	
	private ParametersWithIV buildKey(byte [] key, byte[] iv) {
		ParametersWithIV holder = new ParametersWithIV(
				new KeyParameter(key, 0, key.length), 
				iv, 
				0, 
				iv.length);
		return holder;
	}
	
	private void readFully(InputStream in, byte b[], int off, int len)
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
	
	private byte readByte(InputStream in)throws IOException {
		int ch1 = in.read();
		return (byte) ch1;
	}
	
	private int readShort16(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return ((ch1 << 8) + (ch2 << 0));
	}

	private int readInt32(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}
	
	private boolean validateHMac(KeyParameter kp, byte [] hmac, byte [] contents){
		HMac mac = new HMac(digest);
		mac.init(kp);
		mac.update(contents,0,contents.length);
		byte [] result = new byte[hmac.length];
		mac.doFinal(result, 0);
		return Arrays.equals(hmac, result);
	}
	
	public void addAlertListener(AlertListener listener){
		this.alertListeners.add(listener);
	}
	
}
