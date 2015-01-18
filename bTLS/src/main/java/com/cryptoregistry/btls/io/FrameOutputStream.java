/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.btls.io;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import x.org.bouncycastle.crypto.Digest;
import x.org.bouncycastle.crypto.digests.SHA256Digest;
import x.org.bouncycastle.crypto.engines.AESFastEngine;
import x.org.bouncycastle.crypto.io.CipherOutputStream;
import x.org.bouncycastle.crypto.macs.HMac;
import x.org.bouncycastle.crypto.modes.CBCBlockCipher;
import x.org.bouncycastle.crypto.paddings.PKCS7Padding;
import x.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import x.org.bouncycastle.crypto.params.KeyParameter;
import x.org.bouncycastle.crypto.params.ParametersWithIV;

import com.cryptoregistry.btls.BTLSProtocol;
import com.cryptoregistry.proto.frame.OutputFrame;
import com.cryptoregistry.proto.frame.btls.AlertOutputFrame;

/**
 * Write frames to the underlying stream using the normal write() methods
 * 
 * @author Dave
 *
 */
public class FrameOutputStream extends FilterOutputStream {
	
	protected ByteArrayOutputStream buffer;
	protected CipherOutputStream cOut;
	protected byte [] key;
	protected ParametersWithIV params;
	protected KeyParameter macParam;
	protected Digest digest;
	protected PaddedBufferedBlockCipher aesCipher;
	protected SecureRandom rand = new SecureRandom();
	
	protected Lock lock = new ReentrantLock();
	
	// if true each frame will use a unique IV. This requires creating a cipher per frame
	protected boolean IVPerFrame = false;
	
	/**
	 * Generates a random IV. This IV is written into the application message frame
	 * 
	 * @param out
	 * @param key
	 */
	public FrameOutputStream(OutputStream out, byte [] key) {
		super(out);
		lock.lock();
		try {
			buffer = new ByteArrayOutputStream();
			digest = new SHA256Digest();
			this.key = key;
			// done here for testing purposes
			params = buildKeyWithRandomIV(key);
			macParam = buildKey(key);
			CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
			aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
			aesCipher.init(true, params);
			cOut = new CipherOutputStream(buffer,aesCipher);
		}finally {
			lock.unlock();
		}
	}

	/**
	 * Used for testing - supply key and IV
	 * 
	 * @param out
	 * @param key
	 * @param iv
	 */
	public FrameOutputStream(OutputStream out, byte [] key, byte [] iv) {
		super(out);
		lock.lock();
		try {
			buffer = new ByteArrayOutputStream();
			digest = new SHA256Digest();
			
			// done here for testing purposes
			params = buildKeyWithIV(key,iv);
			macParam = buildKey(key);
			CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
			aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
			aesCipher.init(true, params);
			cOut = new CipherOutputStream(buffer,aesCipher);
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * General constructor - key acquired via handshake protocol
	 * 
	 * @param out
	 */
	public FrameOutputStream(OutputStream out) {
		super(out);
		buffer = new ByteArrayOutputStream();
		digest = new SHA256Digest();
	}
	
	
	/**
	 * Direct write access for sending a custom frame - does not encrypt etc.
	 * @param frame
	 * @throws IOException
	 */
	public void writeOutputFrame(OutputFrame frame) throws IOException{
		lock.lock();
		try {
			frame.writeFrame(out);
			out.flush();
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Alerts are not encrypted, but we do authenticate them with an HMac
	 * 
	 * @param subcode
	 * @param msg
	 */
	public void writeAlert(int subcode,String msg){
		lock.lock();
		try {
			HMac mac = new HMac(digest);
			mac.init(macParam);
			byte[] msgBytes = null;
			try {
				msgBytes = msg.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {}
			mac.update(msgBytes, 0, msgBytes.length);
			byte [] hmac = new byte[digest.getDigestSize()];
			mac.doFinal(hmac, 0);
			AlertOutputFrame frame = new AlertOutputFrame(BTLSProtocol.ALERT, subcode, msg, hmac);
			frame.writeFrame(out);
			digest.reset();
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Alerts are not encrypted but we authenticate with an HMac
	 * 
	 * @param msg
	 */
	public void writeInformationalAlert(String msg){
		writeAlert(BTLSProtocol.INFORMATION, msg);
	}
	
	protected void writeShort(OutputStream out, int v) throws IOException {
		lock.lock();
		try {
			short s = (short) v;
			out.write((s >>>  8) & 0xFF);
			out.write((s >>>  0) & 0xFF);
		}finally{
			lock.unlock();
		}
    }
	
	protected void writeInt(OutputStream out, int v) throws IOException {
		lock.lock();
		try {
			out.write((v >>> 24) & 0xFF);
			out.write((v >>> 16) & 0xFF);
			out.write((v >>>  8) & 0xFF);
			out.write((v >>>  0) & 0xFF);
		}finally{
			lock.unlock();
		}
    }
	
	public void write(int in) throws IOException{
		lock.lock();
		try {
			if(buffer.size()+1 >= Integer.MAX_VALUE) {
				flush(); // force the creation of a new frame
			}
			cOut.write(in);
		}finally {
			lock.unlock();
		}
	}
	
	public void write(byte [] in) throws IOException{
		lock.lock();
		try {
			if(buffer.size()+in.length >= Integer.MAX_VALUE) {
				flush(); // force the creation of a new frame
			}
			cOut.write(in);
		}finally{
			lock.unlock();
		}
	}
	
	public void write(byte[] in, int offset, int length) throws IOException {
		lock.lock();
		try {
			if(buffer.size()+length >= Integer.MAX_VALUE) {
				flush(); // force the creation of a new frame
			}
			cOut.write(in);  
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * Controls the bytes written to the internal buffer. Calling flush() 
	 * forces the CipherOutputStream to call close(), which in turn does a doFinal() and 
	 * completes the encryption for that message. We then generate a frame with an 
	 * Application contentType and send it.
	 * 
	 * @throws IOException 
	 */
	public void flush() throws IOException {
		lock.lock();
		try {
			cOut.flush();
			cOut.close(); // complete any encryption
			
			byte [] payload = buffer.toByteArray();
			
			HMac mac = new HMac(digest);
			mac.init(macParam);
			mac.update(payload, 0, payload.length);
			byte [] hmac = new byte[digest.getDigestSize()];
			mac.doFinal(hmac, 0);
			
			// start writing application msg frame -
			// application content type
			out.write(BTLSProtocol.APPLICATION);
			
			// first write the iv size and iv bytes
			writeShort(out, params.getIV().length);
			out.write(params.getIV(), 0, params.getIV().length);
			
			// write the length of the payload and then the payload itself (encrypted)
			writeInt(out,payload.length);
			out.write(payload);
			
			// first write the hmac size and then the bytes
			writeShort(out, hmac.length);
			out.write(hmac, 0, hmac.length);
			
			out.flush();
			
			// frame complete, reset and cleanup locally
			
			if(IVPerFrame){
				// build a new cipher with a new random IV - more expensive than reset()
				params = this.buildKeyWithRandomIV(key);
				CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
				aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
				aesCipher.init(true, params);
				cOut = new CipherOutputStream(buffer,aesCipher);
				digest.reset();
			}else{
				buffer = new ByteArrayOutputStream();
				aesCipher.reset();
				cOut = new CipherOutputStream(buffer,aesCipher);
				digest.reset();
			}
			
			
		}finally{
			lock.unlock();
		}
		
	}
	
	private ParametersWithIV buildKeyWithRandomIV(byte [] key) {
		byte [] iv = new byte[16];
		rand.nextBytes(iv);
		ParametersWithIV holder = new ParametersWithIV(
				new KeyParameter(key, 0, key.length), 
				iv, 
				0, 
				iv.length);
		return holder;
	}
	
	private ParametersWithIV buildKeyWithIV(byte [] key, byte [] iv) {
		ParametersWithIV holder = new ParametersWithIV(
				new KeyParameter(key, 0, key.length), 
				iv, 
				0, 
				iv.length);
		return holder;
	}
	
	private KeyParameter buildKey(byte [] key) {
			return new KeyParameter(key, 0, key.length);	
	}

	public void setIVPerFrame(boolean iVPerFrame) {
		IVPerFrame = iVPerFrame;
	}

}
