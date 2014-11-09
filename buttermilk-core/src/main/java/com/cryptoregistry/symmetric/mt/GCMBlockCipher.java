/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2014 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.symmetric.mt;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.cryptoregistry.util.StopWatch;

import x.org.bouncycastle.crypto.BlockCipher;
import x.org.bouncycastle.crypto.DataLengthException;
import x.org.bouncycastle.crypto.InvalidCipherTextException;
import x.org.bouncycastle.crypto.engines.AESFastEngine;
import x.org.bouncycastle.crypto.modes.gcm.GCMExponentiator;
import x.org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import x.org.bouncycastle.crypto.modes.gcm.Tables1kGCMExponentiator;
import x.org.bouncycastle.crypto.modes.gcm.Tables8kGCMMultiplier;
import x.org.bouncycastle.crypto.params.AEADParameters;
import x.org.bouncycastle.crypto.params.KeyParameter;
import x.org.bouncycastle.crypto.params.ParametersWithIV;
import x.org.bouncycastle.crypto.util.Pack;
import x.org.bouncycastle.util.Arrays;

/**
 * Implements the Galois/Counter mode (GCM) detailed in NIST Special Publication 800-38D. This is straight from BC, 
 * just copied in for convenience.
 * 
 */
class GCMBlockCipher {
	
	
	static Lock lock = new ReentrantLock();
	
	public static final String gcm_stopwatch = "com.cryptoregistry.crypto.mt.GCMBlockCipher.init";
	private static final int BLOCK_SIZE = 16;

	final BlockCipher cipher;
	final GCMMultiplier multiplier;
	final GCMExponentiator exp;

	// These fields are not modified by processing
	final boolean forEncryption;
	final int macSize;
	final byte[] nonce;
	final byte[] initialAssociatedText;
	final byte[] H;
	final byte[] J0;

	// These fields are modified during processing
	private byte[] bufBlock;
	private byte[] macBlock;
	private byte[] S, S_at, S_atPre;
	private byte[] counter;
	private int bufOff;
	private long totalLength;
	private byte[] atBlock;
	private int atBlockPos;
	private long atLength;
	private long atLengthPre;

//	public GCMBlockCipher(BlockCipher c) {
//		this(c, new Tables8kGCMMultiplier(), new Tables1kGCMExponentiator());
//	}
	
	public static GCMBlockCipher aesgcm(boolean forEncryption, AEADParameters param){
		return new GCMBlockCipher(
				new AESFastEngine(), 
				new Tables8kGCMMultiplier(), 
				new Tables1kGCMExponentiator(), 
				forEncryption, 
				param);
	}
	
	public static GCMBlockCipher aesgcm(boolean forEncryption, ParametersWithIV param){
		lock.lock();
		try {
		StopWatch.INSTANCE.start(gcm_stopwatch);
		GCMBlockCipher cipher = new GCMBlockCipher(
				new AESFastEngine(), 
				new Tables8kGCMMultiplier(), 
				new Tables1kGCMExponentiator(), 
				forEncryption, 
				param);
		StopWatch.INSTANCE.stop(gcm_stopwatch);
		return cipher;
		}finally{
			lock.unlock();
		}
	}


	private GCMBlockCipher(BlockCipher c, GCMMultiplier m, GCMExponentiator exp, boolean forEncryption, AEADParameters param) {
		this.exp = exp;
		this.cipher = c;
		this.multiplier = m;
		this.forEncryption = forEncryption;
		this.macBlock = null;

		KeyParameter keyParam = param.getKey();
		nonce = param.getNonce();
		initialAssociatedText = param.getAssociatedText();

		int macSizeBits = param.getMacSize();
		macSize = macSizeBits / 8;

		int bufLength = forEncryption ? BLOCK_SIZE : (BLOCK_SIZE + macSize);
		this.bufBlock = new byte[bufLength];
		cipher.init(true, keyParam);
		this.H = new byte[BLOCK_SIZE];
		cipher.processBlock(H, 0, H, 0);
		multiplier.init(H);

		this.J0 = new byte[BLOCK_SIZE];

		if (nonce.length == 12) {
			System.arraycopy(nonce, 0, J0, 0, nonce.length);
			this.J0[BLOCK_SIZE - 1] = 0x01;
		} else {
			gHASH(J0, nonce, nonce.length);
			byte[] X = new byte[BLOCK_SIZE];
			Pack.longToBigEndian((long) nonce.length * 8, X, 8);
			gHASHBlock(J0, X);
		}

		this.S = new byte[BLOCK_SIZE];
		this.S_at = new byte[BLOCK_SIZE];
		this.S_atPre = new byte[BLOCK_SIZE];
		this.atBlock = new byte[BLOCK_SIZE];
		this.atBlockPos = 0;
		this.atLength = 0;
		this.atLengthPre = 0;
		this.counter = Arrays.clone(J0);
		this.bufOff = 0;
		this.totalLength = 0;

		if (initialAssociatedText != null) {
			processAADBytes(initialAssociatedText, 0,
					initialAssociatedText.length);
		}
	}
	
	private GCMBlockCipher(BlockCipher c, GCMMultiplier m, GCMExponentiator exp, boolean forEncryption, ParametersWithIV param) {
		this.exp = exp;
		this.cipher = c;
		this.multiplier = m;
		this.forEncryption = forEncryption;
		this.macBlock = null;

		KeyParameter keyParam = (KeyParameter) param.getParameters();
		nonce = param.getIV();
		initialAssociatedText = null;
		macSize = 16;

		int bufLength = forEncryption ? BLOCK_SIZE : (BLOCK_SIZE + macSize);
		this.bufBlock = new byte[bufLength];
		cipher.init(true, keyParam);
		this.H = new byte[BLOCK_SIZE];
		cipher.processBlock(H, 0, H, 0);
		multiplier.init(H);
		this.J0 = new byte[BLOCK_SIZE];

		if (nonce.length == 12) {
			System.arraycopy(nonce, 0, J0, 0, nonce.length);
			this.J0[BLOCK_SIZE - 1] = 0x01;
		} else {
			gHASH(J0, nonce, nonce.length);
			byte[] X = new byte[BLOCK_SIZE];
			Pack.longToBigEndian((long) nonce.length * 8, X, 8);
			gHASHBlock(J0, X);
		}

		this.S = new byte[BLOCK_SIZE];
		this.S_at = new byte[BLOCK_SIZE];
		this.S_atPre = new byte[BLOCK_SIZE];
		this.atBlock = new byte[BLOCK_SIZE];
		this.atBlockPos = 0;
		this.atLength = 0;
		this.atLengthPre = 0;
		this.counter = Arrays.clone(J0);
		this.bufOff = 0;
		this.totalLength = 0;

		if (initialAssociatedText != null) {
			processAADBytes(initialAssociatedText, 0,
					initialAssociatedText.length);
		}
	}


	public int getOutputSize(int len) {
		int totalData = len + bufOff;

		if (forEncryption) {
			return totalData + macSize;
		}

		return totalData < macSize ? 0 : totalData - macSize;
	}

	private void processAADBytes(byte[] in, int inOff, int len) {
		for (int i = 0; i < len; ++i) {
			atBlock[atBlockPos] = in[inOff + i];
			if (++atBlockPos == BLOCK_SIZE) {
				// Hash each block as it fills
				gHASHBlock(S_at, atBlock);
				atBlockPos = 0;
				atLength += BLOCK_SIZE;
			}
		}
	}

	private void initCipher() {
		if (atLength > 0) {
			System.arraycopy(S_at, 0, S_atPre, 0, BLOCK_SIZE);
			atLengthPre = atLength;
		}

		// Finish hash for partial AAD block
		if (atBlockPos > 0) {
			gHASHPartial(S_atPre, atBlock, 0, atBlockPos);
			atLengthPre += atBlockPos;
		}

		if (atLengthPre > 0) {
			System.arraycopy(S_atPre, 0, S, 0, BLOCK_SIZE);
		}
	}

	public int processBytes(byte[] in, int inOff, int len, byte[] out,
			int outOff) throws DataLengthException {
		int resultLen = 0;

		for (int i = 0; i < len; ++i) {
			bufBlock[bufOff] = in[inOff + i];
			if (++bufOff == bufBlock.length) {
				outputBlock(out, outOff + resultLen);
				resultLen += BLOCK_SIZE;
			}
		}

		return resultLen;
	}

	private void outputBlock(byte[] output, int offset) {
		if (totalLength == 0) {
			initCipher();
		}
		gCTRBlock(bufBlock, output, offset);
		if (forEncryption) {
			bufOff = 0;
		} else {
			System.arraycopy(bufBlock, BLOCK_SIZE, bufBlock, 0, macSize);
			bufOff = macSize;
		}
	}

	public int doFinal(byte[] out, int outOff) throws IllegalStateException,
			InvalidCipherTextException {
		if (totalLength == 0) {
			initCipher();
		}

		int extra = bufOff;
		if (!forEncryption) {
			if (extra < macSize) {
				throw new InvalidCipherTextException("data too short");
			}
			extra -= macSize;
		}

		if (extra > 0) {
			gCTRPartial(bufBlock, 0, extra, out, outOff);
		}

		atLength += atBlockPos;

		if (atLength > atLengthPre) {
			/*
			 * Some AAD was sent after the cipher started. We determine the
			 * difference b/w the hash value we actually used when the cipher
			 * started (S_atPre) and the final hash value calculated (S_at).
			 * Then we carry this difference forward by multiplying by H^c,
			 * where c is the number of (full or partial) cipher-text blocks
			 * produced, and adjust the current hash.
			 */

			// Finish hash for partial AAD block
			if (atBlockPos > 0) {
				gHASHPartial(S_at, atBlock, 0, atBlockPos);
			}

			// Find the difference between the AAD hashes
			if (atLengthPre > 0) {
				xor(S_at, S_atPre);
			}

			// Number of cipher-text blocks produced
			long c = ((totalLength * 8) + 127) >>> 7;

			// Calculate the adjustment factor
			byte[] H_c = new byte[16];
			exp.exponentiateX(c, H_c);

			// Carry the difference forward
			multiply(S_at, H_c);

			// Adjust the current hash
			xor(S, S_at);
		}

		// Final gHASH
		byte[] X = new byte[BLOCK_SIZE];
		Pack.longToBigEndian(atLength * 8, X, 0);
		Pack.longToBigEndian(totalLength * 8, X, 8);

		gHASHBlock(S, X);

		// TODO Fix this if tagLength becomes configurable
		// T = MSBt(GCTRk(J0,S))
		byte[] tag = new byte[BLOCK_SIZE];
		cipher.processBlock(J0, 0, tag, 0);
		xor(tag, S);

		int resultLen = extra;

		// We place into macBlock our calculated value for T
		this.macBlock = new byte[macSize];
		System.arraycopy(tag, 0, macBlock, 0, macSize);

		if (forEncryption) {
			// Append T to the message
			System.arraycopy(macBlock, 0, out, outOff + bufOff, macSize);
			resultLen += macSize;
		} else {
			// Retrieve the T value from the message and compare to calculated
			// one
			byte[] msgMac = new byte[macSize];
			System.arraycopy(bufBlock, extra, msgMac, 0, macSize);
			if (!Arrays.constantTimeAreEqual(this.macBlock, msgMac)) {
				throw new InvalidCipherTextException("mac check in GCM failed");
			}
		}

		return resultLen;
	}

	

	private void gCTRBlock(byte[] block, byte[] out, int outOff) {
		byte[] tmp = getNextCounterBlock();

		xor(tmp, block);
		System.arraycopy(tmp, 0, out, outOff, BLOCK_SIZE);

		gHASHBlock(S, forEncryption ? tmp : block);

		totalLength += BLOCK_SIZE;
	}

	private void gCTRPartial(byte[] buf, int off, int len, byte[] out,
			int outOff) {
		byte[] tmp = getNextCounterBlock();

		xor(tmp, buf, off, len);
		System.arraycopy(tmp, 0, out, outOff, len);

		gHASHPartial(S, forEncryption ? tmp : buf, 0, len);

		totalLength += len;
	}

	private void gHASH(byte[] Y, byte[] b, int len) {
		for (int pos = 0; pos < len; pos += BLOCK_SIZE) {
			int num = Math.min(len - pos, BLOCK_SIZE);
			gHASHPartial(Y, b, pos, num);
		}
	}

	private void gHASHBlock(byte[] Y, byte[] b) {
		xor(Y, b);
		multiplier.multiplyH(Y);
	}

	private void gHASHPartial(byte[] Y, byte[] b, int off, int len) {
		xor(Y, b, off, len);
		multiplier.multiplyH(Y);
	}

	private byte[] getNextCounterBlock() {
		for (int i = 15; i >= 12; --i) {
			byte b = (byte) ((counter[i] + 1) & 0xff);
			counter[i] = b;

			if (b != 0) {
				break;
			}
		}

		byte[] tmp = new byte[BLOCK_SIZE];
		// TODO Sure would be nice if ciphers could operate on int[]
		cipher.processBlock(counter, 0, tmp, 0);
		return tmp;
	}

	private static void multiply(byte[] block, byte[] val) {
		byte[] tmp = Arrays.clone(block);
		byte[] c = new byte[16];

		for (int i = 0; i < 16; ++i) {
			byte bits = val[i];
			for (int j = 7; j >= 0; --j) {
				if ((bits & (1 << j)) != 0) {
					xor(c, tmp);
				}

				boolean lsb = (tmp[15] & 1) != 0;
				shiftRight(tmp);
				if (lsb) {
					// R = new byte[]{ 0xe1, ... };
					// xor(v, R);
					tmp[0] ^= (byte) 0xe1;
				}
			}
		}

		System.arraycopy(c, 0, block, 0, 16);
	}

	private static void shiftRight(byte[] block) {
		int i = 0;
		int bit = 0;
		for (;;) {
			int b = block[i] & 0xff;
			block[i] = (byte) ((b >>> 1) | bit);
			if (++i == 16) {
				break;
			}
			bit = (b & 1) << 7;
		}
	}

	private static void xor(byte[] block, byte[] val) {
		for (int i = 15; i >= 0; --i) {
			block[i] ^= val[i];
		}
	}

	private static void xor(byte[] block, byte[] val, int off, int len) {
		while (len-- > 0) {
			block[len] ^= val[off + len];
		}
	}
}
