package com.cryptoregistry.app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.signature.CryptoSignature;

/**
 * Simple container to collect what we generate
 * 
 * @author Dave
 *
 */
public class KM {
	
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private String regHandle;
	private char [] password;
	private KeyGenerationAlgorithm keyAlg;
	private PBEAlg pbeAlg;
	private CryptoKey secureKey;
	private CryptoKey keyForPublication;
	private List<CryptoContact> contacts;
	private CryptoSignature signature;
	
	public KM() {
		contacts = new ArrayList<CryptoContact>();
	}

	public String getRegHandle() {
		return regHandle;
	}

	public void setRegHandle(String regHandle) {
		pcs.firePropertyChange("regHandle", this.regHandle, regHandle);
		this.regHandle = regHandle;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		pcs.firePropertyChange("password", this.password, password);
		this.password = password;
	}

	public KeyGenerationAlgorithm getKeyAlg() {
		return keyAlg;
	}

	public void setKeyAlg(KeyGenerationAlgorithm keyAlg) {
		pcs.firePropertyChange("keyAlg", this.keyAlg, keyAlg);
		this.keyAlg = keyAlg;
	}

	public PBEAlg getPbeAlg() {
		return pbeAlg;
	}

	public void setPbeAlg(PBEAlg pbeAlg) {
		pcs.firePropertyChange("pbeAlg", this.pbeAlg, pbeAlg);
		this.pbeAlg = pbeAlg;
	}

	public CryptoKey getSecureKey() {
		return secureKey;
	}

	public void setSecureKey(CryptoKey secureKey) {
		pcs.firePropertyChange("secureKey", this.secureKey, secureKey);
		this.secureKey = secureKey;
	}

	public CryptoKey getKeyForPublication() {
		return keyForPublication;
	}

	public void setKeyForPublication(CryptoKey keyForPublication) {
		pcs.firePropertyChange("keyForPublication", this.keyForPublication, keyForPublication);
		this.keyForPublication = keyForPublication;
	}

	public CryptoSignature getSignature() {
		return signature;
	}

	public void setSignature(CryptoSignature signature) {
		pcs.firePropertyChange("signature", this.signature, signature);
		this.signature = signature;
	}
	
	public void addContact(CryptoContact contact){
		this.contacts.add(contact);
		pcs.firePropertyChange("contacts", null, contact);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		pcs.addPropertyChangeListener(listener);
	}
	
}
