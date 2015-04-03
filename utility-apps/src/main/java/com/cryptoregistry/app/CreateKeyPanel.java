/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.JComboBox;

import asia.redact.bracket.properties.Obfuscate;
import asia.redact.bracket.properties.OutputAdapter;
import asia.redact.bracket.properties.PlainOutputFormat;
import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.Buttermilk;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.ec.ECKeyMetadata;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.rsa.RSAKeyMetadata;
import com.cryptoregistry.util.Check10K;
import com.cryptoregistry.util.entropy.TresBiEntropy;
import com.cryptoregistry.util.entropy.TresBiEntropy.Result;

import javax.swing.JCheckBox;

public class CreateKeyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	JComboBox<KeyGenerationAlgorithm> keyalgComboBox;
	JComboBox<PBEAlg> pbeAlgComboBox;
	private JPasswordField password0;
	private JPasswordField password1;
	JLabel passwordEqualityMsg;
	JLabel againLbl;
	JLabel lblEntropy;
	JButton btnCreate;
	JCheckBox chckbxCreateObfuscatedPassword;
	
	private Check10K tenK;
	
	private KeyGenerationAlgorithm keyAlg;
	private CryptoKey secureKey;
	private CryptoKey keyForPublication;
	
	
	public CreateKeyPanel(Properties props){
		super();
		
		tenK = new Check10K();
		
		JLabel lblPassword = new JLabel("Password");
		JLabel lblAgain = new JLabel("Again");
		
		password0 = new JPasswordField("");
		password0.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkPassword();
				checkEntropy();
			}
		});
		password0.setColumns(10);
		password1 = new JPasswordField("");
		password1.setColumns(10);
		password1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkPassword();
				checkEntropy();
			}
		});
		
		lblEntropy = new JLabel("Entropy: 0 bits");
		
		btnCreate = new JButton("Create Key");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = password0.getPassword().length;
				if(count == 0) {
					JOptionPane.showMessageDialog((JButton)e.getSource(),
						    "Password required here to proceed.",
						    "Request",
						    JOptionPane.WARNING_MESSAGE);
					password0.requestFocusInWindow();
					return;
				}
					
				btnCreate.setText("Working...");
				btnCreate.setEnabled(false);
				SwingWorker<Void,String> worker = new SwingWorker<Void,String>() {
					protected Void doInBackground() throws Exception {
						createKey();
						return null;
					}
					 @Override
					public void done() {
					  try {
							get();
							
							SwingRegistrationWizardGUI.km.setPassword(password0.getPassword());
							SwingRegistrationWizardGUI.km.setPbeAlg((PBEAlg)pbeAlgComboBox.getSelectedItem());
							SwingRegistrationWizardGUI.km.setKeyAlg(keyAlg);
							SwingRegistrationWizardGUI.km.setKeyForPublication(keyForPublication);
							SwingRegistrationWizardGUI.km.setSecureKey(secureKey);
							 
						  } catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						  }
						 
						 btnCreate.setText("Create Key");
						 btnCreate.setEnabled(true);
						 SwingRegistrationWizardGUI.tabbedPane.setSelectedIndex(4);
					}
				};
				worker.execute();
				
			}
		});
		
		KeyGenerationAlgorithm [] e = KeyGenerationAlgorithm.usableForSignature();
		keyalgComboBox = new JComboBox<KeyGenerationAlgorithm>();
		DefaultComboBoxModel<KeyGenerationAlgorithm> model = new DefaultComboBoxModel<KeyGenerationAlgorithm>(e);
		keyalgComboBox.setModel(model);
		
		PBEAlg [] pbes = PBEAlg.values();
		DefaultComboBoxModel<PBEAlg> pbemodel = new DefaultComboBoxModel<PBEAlg>(pbes);
		pbeAlgComboBox = new JComboBox<PBEAlg>();
		pbeAlgComboBox.setModel(pbemodel);
		
		JLabel lblKeyAlg = new JLabel("Asymmetric Key Algorithm");
		
		againLbl = new JLabel("");
		
		JLabel lblPasswordBasedEncryption = new JLabel("Password Based Encryption Algorithm");
		
		passwordEqualityMsg = new JLabel("...");
		
		chckbxCreateObfuscatedPassword = new JCheckBox("Create obfuscated password file from this value");
		
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(353, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxCreateObfuscatedPassword)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblKeyAlg)
								.addComponent(againLbl)
								.addComponent(lblPasswordBasedEncryption))
							.addGap(31)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(keyalgComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(pbeAlgComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPassword)
								.addComponent(lblAgain))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addComponent(passwordEqualityMsg)
									.addGap(108)
									.addComponent(lblEntropy))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(password1)
									.addComponent(password0, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)))))
					.addContainerGap(45, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblKeyAlg)
						.addComponent(keyalgComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(againLbl)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblPasswordBasedEncryption)
							.addComponent(pbeAlgComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(password0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAgain)
						.addComponent(password1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEntropy)
						.addComponent(passwordEqualityMsg))
					.addGap(28)
					.addComponent(chckbxCreateObfuscatedPassword)
					.addPreferredGap(ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
					.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}


	public JPasswordField getPassword0() {
		return password0;
	}
	
	private void checkPassword() {
		char[]pass0 = password0.getPassword();
		char[]pass1 = password1.getPassword();
		
		if(pass0 == null || pass0.length==0){
			this.passwordEqualityMsg.setForeground(Color.RED);
			this.passwordEqualityMsg.setText("Empty");
			return;
		}
		
		boolean ok = Arrays.equals(pass0, pass1);
		if(ok) {
			this.passwordEqualityMsg.setForeground(Color.BLACK);
			this.passwordEqualityMsg.setText("Match");
			this.btnCreate.setEnabled(true);
			if(tenK.contains(new String(pass0))){
				this.passwordEqualityMsg.setForeground(Color.RED);
				this.passwordEqualityMsg.setText("In 10K List!");
				this.btnCreate.setEnabled(false);
			}
		}else{
			this.passwordEqualityMsg.setForeground(Color.RED);
			this.passwordEqualityMsg.setText("Input not equal");
			this.btnCreate.setEnabled(false);
		}
	}
	
	private void checkEntropy() {
		char[]pass0 = password0.getPassword();
		if(pass0 == null || pass0.length ==0) {
			this.lblEntropy.setText("Entropy: 0 bits");
			return;
		}
		byte [] bytes = toBytes(pass0);
		TresBiEntropy bi = new TresBiEntropy(bytes);
		Result res = bi.calc();
		int entropy = (int) res.bitsOfEntropy;
		this.lblEntropy.setText("Entropy: "+entropy+" bits");
	}
	
	private byte[] toBytes(char[] chars) {
	    CharBuffer charBuffer = CharBuffer.wrap(chars);
	    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
	            byteBuffer.position(), byteBuffer.limit());
	    Arrays.fill(charBuffer.array(), '\u0000'); 
	    Arrays.fill(byteBuffer.array(), (byte) 0);
	    return bytes;
	}
	
	private void createKey(){
		
		// check the directory destination
		File kmDir = new File(SwingRegistrationWizardGUI.settingsPanel.getTextField().getText().trim());
		if(!kmDir.exists()){
			kmDir.mkdirs();
		}
		
		// get the proposed Reg Handle
		String regHandle = SwingRegistrationWizardGUI.regHandlePanel.getRegHandleTextField().getText().trim();
		if(regHandle == null || regHandle.trim().length() == 0) {
			System.err.println("registration handle not defined, please do that first.");
			return;
		}
		
		// gather the required values
		KeyGenerationAlgorithm keyAlg = (KeyGenerationAlgorithm) keyalgComboBox.getSelectedItem();
		PBEAlg pbeAlg = (PBEAlg) pbeAlgComboBox.getSelectedItem();
		char [] password = password0.getPassword();
		
		// 1.0 - create obfuscated password file if required
		
		boolean createObfuscatedPassword = chckbxCreateObfuscatedPassword.isSelected();
		if(createObfuscatedPassword){
			File passwordFile = new File(kmDir,"password.properties");
			String passwordBase64 = Obfuscate.FACTORY.encrypt(password);
			Properties props = Properties.Factory.getInstance();
			props.put("password", passwordBase64);
			OutputAdapter out = new OutputAdapter(props);
			out.writeTo(passwordFile, new PlainOutputFormat(), Charset.forName("UTF-8"));
			passwordFile.setExecutable(false);
		//	passwordFile.setWritable(false);
			passwordFile.setReadable(true, true);
			try {
				System.out.println("Wrote password file to "+ passwordFile.getCanonicalPath());
			} catch (IOException e) {}
		}
		
		
		// 2.0 -- create Key of desired type
		switch(keyAlg){
			case Curve25519: {
				
				C2KeyMetadata meta = null;
				if(pbeAlg == PBEAlg.PBKDF2) {
					meta = C2KeyMetadata.createSecurePBKDF2(password);
				}else if(pbeAlg == PBEAlg.SCRYPT){
					meta = C2KeyMetadata.createSecureScrypt(password);
				}
				Curve25519KeyContents contents = Buttermilk.INSTANCE.generateC2Keys(meta);
				Curve25519KeyForPublication pub = contents.cloneForPublication();
				this.keyAlg = keyAlg;
				this.keyForPublication = pub;
				this.secureKey = contents;
				break;
			}
			case EC: {
				ECKeyMetadata meta = null;
				if(pbeAlg == PBEAlg.PBKDF2) {
					meta = ECKeyMetadata.createSecurePBKDF2(password);
				}else if(pbeAlg == PBEAlg.SCRYPT){
					meta = ECKeyMetadata.createSecureScrypt(password);
				}
				ECKeyContents contents = Buttermilk.INSTANCE.generateECKeys(meta, "P-256");
				ECKeyForPublication pub = contents.cloneForPublication();
				this.keyAlg = keyAlg;
				this.keyForPublication = pub;
				this.secureKey = contents;
				break;
			}
			case RSA: {
				RSAKeyMetadata meta = null;
				if(pbeAlg == PBEAlg.PBKDF2) {
					meta = RSAKeyMetadata.createSecurePBKDF2(password);
				}else if(pbeAlg == PBEAlg.SCRYPT){
					meta = RSAKeyMetadata.createSecureScrypt(password);
				}
				RSAKeyContents contents = Buttermilk.INSTANCE.generateRSAKeys(meta);
				RSAKeyForPublication pub = contents.cloneForPublication();
				this.keyAlg = keyAlg;
				this.keyForPublication = pub;
				this.secureKey = contents;
				break;
			}
			
			default: {}
		}
		
	}
	
	/**
	private String writeFile(File parent, String prefix, JSONFormatter builder){
		String output = null;
		Date date = new Date();
		String d = String.valueOf(date.getTime());
		StringWriter writer = new StringWriter();
		builder.format(writer);
		File forPublicationFile = new File(parent,"request-"+d+".json");
		output = writer.toString();
		byte [] bytes = null;
		try {
			bytes = output.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {}
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(forPublicationFile);
			out.write(bytes, 0, bytes.length);
		}catch(IOException x){
			x.printStackTrace();
		}finally{
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {}
		}
		
		return output;
	}
	
	*/
	
}
