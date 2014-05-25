package com.cryptoregistry.pbe;


import x.org.bouncycastle.crypto.PBEParametersGenerator;
import x.org.bouncycastle.crypto.engines.AESEngine;
import x.org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import x.org.bouncycastle.crypto.generators.SCrypt;
import x.org.bouncycastle.crypto.modes.CBCBlockCipher;
import x.org.bouncycastle.crypto.paddings.PKCS7Padding;
import x.org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import x.org.bouncycastle.crypto.params.KeyParameter;
import x.org.bouncycastle.crypto.params.ParametersWithIV;

/**
 * Do password based encryption.This is used to encapsulate generated key materials.
 * 
 * There are two key derivation routines to choose from: PBKDF2 and SCRYPT. The
 * encryption algorithm is AES/PKCS7 with a 256 bit key.
 * 
 * @author Dave
 *
 */
public class PBE {

	PBEParams params;
	public static final int KEY_SIZE = 256; // aes key size in bits
	public static final int IV_SIZE = 128; // iv size in bits

	/**
	 * rand is used with scrypt to generate the IV, but not needed for PBKDF2, so you can set it to null in that case
	 * @param params
	 * @param rand
	 */
	public PBE(PBEParams params){
		this.params = params;
	}

	public ArmoredPBEResult encrypt(byte [] input) {
		ParametersWithIV holder = this.buildKey();
		CBCBlockCipher blockCipher = new CBCBlockCipher(new AESEngine());
		PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
		aesCipher.init(true, holder);
		byte [] encrypted = genCipherData(aesCipher, input);
		switch(params.getAlg()){
			case PBKDF2: {
				return new ArmoredPBKDF2Result(encrypted,params.getSalt().getData(),params.getIterations());
			}
			case SCRYPT: {
				return new ArmoredScryptResult(
						encrypted,
						params.getSalt().getData(),
						params.getIv().getData(),
						params.getCpuMemoryCost_N(),
						params.getBlockSize_r(),
						params.getParallelization_p()
						);
			}
			default : throw new RuntimeException("No such alg yet defined in this method: "+params.getAlg());
		}
	}
	
	public byte [] decrypt(byte [] encrypted) {
		ParametersWithIV holder = this.buildKey();
		CBCBlockCipher blockCipher = new CBCBlockCipher(new AESEngine());
		PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(blockCipher, new PKCS7Padding());
		aesCipher.init(false, holder);
		byte [] plain = genCipherData(aesCipher, encrypted);
		return plain;
	}
	
	private byte[] genCipherData(PaddedBufferedBlockCipher cipher, byte[] data) {
	    int minSize = cipher.getOutputSize(data.length);
	    byte[] outBuf = new byte[minSize];
	    int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
	    int length2 = cipher.doFinal(outBuf, length1);
	    int actualLength = length1 + length2;
	    byte[] result = new byte[actualLength];
	    System.arraycopy(outBuf, 0, result, 0, result.length);
	    return result;
	}
	
	private ParametersWithIV buildKey() {
		
		ParametersWithIV holder = null;
		
		switch(params.getAlg()){
			case PBKDF2: {
				PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
				generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(
					params.getPassword().getPassword()), 
					params.getSalt().getData(), 
					params.getIterations());
				holder = (ParametersWithIV) generator.generateDerivedParameters(KEY_SIZE, IV_SIZE);
				break;
			}
			case SCRYPT: {
				byte [] key = SCrypt.generate(
						params.getPassword().toBytes(), 
						params.getSalt().getData(), 
						params.getCpuMemoryCost_N(),
						params.getBlockSize_r(),
						params.getParallelization_p(), 
						params.getDesiredKeyLengthInBytes());
				
				holder = new ParametersWithIV(
						new KeyParameter(key, 0, key.length), 
						params.getIv().getData(), 
						0, 
						params.getIv().length()
				);
				break;
			}
			default : throw new RuntimeException("No such alg yet defined in this method: "+params.getAlg());
		}
		
		return holder;
	}

}
