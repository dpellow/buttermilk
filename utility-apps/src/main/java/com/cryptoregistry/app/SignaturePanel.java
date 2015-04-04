/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;


import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.SwingWorker;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import org.apache.http.client.utils.URIBuilder;

import com.cryptoregistry.CryptoContact;
import com.cryptoregistry.CryptoKey;
import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.MapData;
import com.cryptoregistry.c2.key.Curve25519KeyForPublication;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.ec.ECKeyForPublication;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.rsa.RSAKeyContents;
import com.cryptoregistry.rsa.RSAKeyForPublication;
import com.cryptoregistry.signature.C2CryptoSignature;
import com.cryptoregistry.signature.CryptoSignature;
import com.cryptoregistry.signature.ECDSACryptoSignature;
import com.cryptoregistry.signature.RSACryptoSignature;
import com.cryptoregistry.signature.builder.C2KeyContentsIterator;
import com.cryptoregistry.signature.builder.C2SignatureBuilder;
import com.cryptoregistry.signature.builder.ContactContentsIterator;
import com.cryptoregistry.signature.builder.ECDSASignatureBuilder;
import com.cryptoregistry.signature.builder.ECKeyContentsIterator;
import com.cryptoregistry.signature.builder.MapDataContentsIterator;
import com.cryptoregistry.signature.builder.RSAKeyContentsIterator;
import com.cryptoregistry.signature.builder.RSASignatureBuilder;
import com.cryptoregistry.util.MapIterator;

import asia.redact.bracket.properties.Properties;

public class SignaturePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea textArea;
	JButton btnCreateSignature;
	JCheckBox chckbxIAgreeTo;
	JCheckBox chckbxIAffirmThe ;

	private CryptoSignature signature;
	private MapData affirmations;
	
	private JSONFormatter requestFormatter;
	private JSONFormatter secureKeyFormatter;

	public SignaturePanel(final Properties props) {
		super();
		
		JLabel lblCopyrightStatement = new JLabel("Copyright Statement");
		
		textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText("Copyright 2015 by <Your Legal Name>. All Rights Reserved");
		
		btnCreateSignature = new JButton("Create Signature");
		chckbxIAgreeTo = new JCheckBox("I agree to cryptoregistry.com's Terms of Service");
		chckbxIAffirmThe = new JCheckBox("I affirm the information I have entered is valid and correct.");
		
		chckbxIAgreeTo.setSelected(false);
		chckbxIAgreeTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxIAgreeTo.isSelected() && chckbxIAffirmThe.isSelected()) {
					btnCreateSignature.setEnabled(true);
				}else{
					btnCreateSignature.setEnabled(false);
				}
			}
		});
		
		JButton btnTermsOfService = new JButton("Show");
		btnTermsOfService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					open(url(props));
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnCreateSignature.setEnabled(false);
		btnCreateSignature.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCreateSignature.setText("Working...");
				btnCreateSignature.setEnabled(false);
				SwingWorker<Void,String> worker = new SwingWorker<Void,String>() {
					protected Void doInBackground() throws Exception {
						createSignature();
						writeRequest();
						return null;
					}
					 @Override
					public void done() {
					  try {
							get();
							if(signature != null)
								SwingRegistrationWizardGUI.km.setSignature(signature);
						  } catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						  }
						 
						 btnCreateSignature.setText("Create Signature");
						 btnCreateSignature.setEnabled(true);
						 SwingRegistrationWizardGUI.tabbedPane.setSelectedIndex(8);
					}
				};
				worker.execute();
			}
		});
		
		chckbxIAffirmThe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(chckbxIAgreeTo.isSelected() && chckbxIAffirmThe.isSelected()) {
					btnCreateSignature.setEnabled(true);
				}else{
					btnCreateSignature.setEnabled(false);
				}
			}
			
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(325, Short.MAX_VALUE)
					.addComponent(btnCreateSignature)
					.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxIAffirmThe)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxIAgreeTo)
							.addGap(31)
							.addComponent(btnTermsOfService))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCopyrightStatement)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(72, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCopyrightStatement))
					.addGap(36)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxIAgreeTo)
						.addComponent(btnTermsOfService))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxIAffirmThe)
					.addPreferredGap(ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
					.addComponent(btnCreateSignature)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	private URI url(Properties props) throws URISyntaxException {
		URIBuilder builder = new URIBuilder();
		 builder.setScheme(props.get("registration.tos.scheme"))
	        .setHost(props.get("registration.tos.hostname"))
	        .setPath(props.get("registration.tos.path"))
	        .setPort(props.intValue("registration.tos.port"));
		return builder.build();
	}

	private void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { }
	    } else { throw new RuntimeException("No Desktop available to launch browser"); }
	}
	
	private void createSignature() {
		
		affirmations = new MapData();
		affirmations.put("Copyright", this.textArea.getText());
		affirmations.put("TermsOfServiceAgreement", this.chckbxIAgreeTo.getText());
		affirmations.put("InfoAffirmation", this.chckbxIAffirmThe.getText());
		
		String regHandle = SwingRegistrationWizardGUI.km.getRegHandle();
		KeyGenerationAlgorithm alg = SwingRegistrationWizardGUI.km.getKeyAlg();
		CryptoKey pubKey = SwingRegistrationWizardGUI.km.getKeyForPublication();
		CryptoKey secureKey = SwingRegistrationWizardGUI.km.getSecureKey();
		List<CryptoContact> contacts = SwingRegistrationWizardGUI.km.getContacts();
		
		requestFormatter = new JSONFormatter(regHandle);
		secureKeyFormatter = new JSONFormatter(regHandle);
		secureKeyFormatter.add(secureKey);
		
		MapIterator iter = null;
		
		switch(alg){
			case Curve25519: {
				Curve25519KeyForPublication pub = (Curve25519KeyForPublication) pubKey;
				C2SignatureBuilder sigBuilder = new C2SignatureBuilder(regHandle, (Curve25519KeyContents) secureKey);
				iter = new C2KeyContentsIterator(pub);
				// key contents
				while(iter.hasNext()){
					String label = iter.next();
					sigBuilder.update(label, iter.get(label));
				}
				requestFormatter.add(pub);
				// contacts
				for(CryptoContact contact: contacts){
					iter = new ContactContentsIterator(contact);
					while(iter.hasNext()){
						String label = iter.next();
						sigBuilder.update(label, iter.get(label));
					}
					requestFormatter.add(contact);
				}
				// affirmations - MapData
				iter = new MapDataContentsIterator(affirmations);
				while(iter.hasNext()){
					String label = iter.next();
					sigBuilder.update(label, iter.get(label));
				}
				requestFormatter.add(affirmations);
				
				C2CryptoSignature sig = sigBuilder.build();
				signature = sig;
				requestFormatter.add(sig);
				
				SwingRegistrationWizardGUI.km.setSignature(sig);
				SwingRegistrationWizardGUI.km.setFormatter(requestFormatter);
				
				break;
			}
			case EC: {
				ECKeyForPublication pub = (ECKeyForPublication) pubKey;
				ECDSASignatureBuilder sigBuilder = new ECDSASignatureBuilder(regHandle, (ECKeyContents) secureKey);
				iter = new ECKeyContentsIterator(pub);
				// key contents
				while(iter.hasNext()){
					String label = iter.next();
					sigBuilder.update(label, iter.get(label));
				}
				requestFormatter.add(pub);
				
				// contacts
				for(CryptoContact contact: contacts){
					iter = new ContactContentsIterator(contact);
					while(iter.hasNext()){
						String label = iter.next();
						sigBuilder.update(label, iter.get(label));
					}
					requestFormatter.add(contact);
				}
				// affirmations - MapData
				iter = new MapDataContentsIterator(affirmations);
				while(iter.hasNext()){
					String label = iter.next();
					sigBuilder.update(label, iter.get(label));
				}
				requestFormatter.add(affirmations);
				
				ECDSACryptoSignature sig = sigBuilder.build();
				signature = sig;
				requestFormatter.add(sig);
				
				SwingRegistrationWizardGUI.km.setSignature(sig);
				SwingRegistrationWizardGUI.km.setFormatter(requestFormatter);
				
				break;
			}
			case RSA: {
				RSAKeyForPublication pub = (RSAKeyForPublication) pubKey;
				RSASignatureBuilder sigBuilder = new RSASignatureBuilder(regHandle, (RSAKeyContents) secureKey);
				iter = new RSAKeyContentsIterator(pub);
				// key contents
				while(iter.hasNext()){
					String label = iter.next();
					sigBuilder.update(label, iter.get(label));
				}
				requestFormatter.add(pub);
				
				// contacts
				for(CryptoContact contact: contacts){
					iter = new ContactContentsIterator(contact);
					while(iter.hasNext()){
						String label = iter.next();
						sigBuilder.update(label, iter.get(label));
					}
					requestFormatter.add(contact);
				}
			
				// affirmations - MapData
				iter = new MapDataContentsIterator(affirmations);
				while(iter.hasNext()){
					String label = iter.next();
					sigBuilder.update(label, iter.get(label));
				}
				requestFormatter.add(affirmations);
				
				RSACryptoSignature sig = sigBuilder.build();
				signature = sig;
				requestFormatter.add(sig);
				
				SwingRegistrationWizardGUI.km.setSignature(sig);
				SwingRegistrationWizardGUI.km.setFormatter(requestFormatter);
				
				break;
			}
				default: { throw new RuntimeException("Not a signature algorithm");
			}
		}// end of switch
	}//end of method
	
	private void writeRequest(){
		 StringWriter writer = new StringWriter();
		 requestFormatter.format(writer);
		 String output = writer.toString();
		 String path = SwingRegistrationWizardGUI.settingsPanel.getParentFolderTextField().getText().trim();
		 File reqFile = new File(new File(path), "request.json.txt");
		 writeFile(reqFile, output);
		 SwingRegistrationWizardGUI.keyMaterialsPanel.getRequestTextPane().setText(output);
		 
		 writer = new StringWriter();
		 secureKeyFormatter.format(writer);
		 output = writer.toString();
		 File securedFile = new File(new File(path), "secureKey.json.txt");
		 writeFile(securedFile, output);
		 SwingRegistrationWizardGUI.keyMaterialsPanel.getSecureKeyTextPane().setText(output);
		 
		
	}
	
	private void writeFile(File file, String contents){
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(contents.getBytes("UTF-8"));
			out.flush();
		}catch(Exception x){
			x.printStackTrace();
		}finally{
			if(out != null)
				try {
					out.close();
				} catch (IOException e) {}
		}
	}
	
	
}
