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

public class FrameInputStream extends FilterInputStream {
	
	protected CipherInputStream cIn;
	protected byte [] key;
	protected ParametersWithIV params;
	protected KeyParameter hmacParam;
	protected Digest digest;
	PaddedBufferedBlockCipher aesCipher;
	
	protected Set<AlertListener> alertListeners;
	
	// general use, key is acquired or constructed via handshake
	public FrameInputStream(InputStream in) {
		super(in);
		digest = new SHA256Digest();
		alertListeners = new HashSet<AlertListener>();
	}

	// for testing only
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
					
					// first there is a Byte String with the IV size and contents
					int IVSz = this.readShort16(in);
					byte [] iv = new byte[IVSz];
					this.readFully(in, iv, 0, IVSz);
					
					// second there is the encrypted contents, max length is Integer.MAX_VALUE, about 2.15Gb
					int contentsSz = this.readInt32(in);
					byte [] contents = new byte[contentsSz];
					this.readFully(in, contents, 0, contentsSz);
					ByteArrayInputStream bin = new ByteArrayInputStream(contents);
					params = buildKey(key,iv);
					
					CBCBlockCipher blockCipher = new CBCBlockCipher(new AESFastEngine());
					aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
					aesCipher.init(false, params);
					cIn = new CipherInputStream(bin,aesCipher);
					
					// cIn can now be read using the read() methods
					
					// third there is an hmac size and hmac bytes
					hmacParam = new KeyParameter(key,0,key.length);
					int macSz = this.readShort16(in);
					byte [] hmacBytes = new byte[macSz];
					this.readFully(in, hmacBytes, 0, macSz);
					if(!validateHMac(hmacParam, hmacBytes,contents)) {
						throw new RuntimeException("Application Frame appears to have been tampered with...bailing out.");
					}
					
					break;
					
				}
				case BTLSProtocol.HANDSHAKE: {
				//	int handshakeCode = this.readShort16(in);
				//	int length = this.readShort16(in);
					break;
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
