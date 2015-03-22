package com.cryptoregistry.app;


import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class SignaturePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	URI uri;


	public SignaturePanel() {
		super();
		
		JLabel lblCopyrightStatement = new JLabel("Copyright Statement");
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText("Copyright 2015 by <Your Legal Name>. All Rights Reserved");
		
		JCheckBox chckbxIAgreeTo = new JCheckBox("I agree to cryptoregistry.com's Terms of Service");
		chckbxIAgreeTo.setSelected(false);
		chckbxIAgreeTo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.err.println("Checkbox checked");
			}
		});
		
		JButton btnTermsOfService = new JButton("Show");
		
		JButton btnCreateSignature = new JButton("Create Signature");
		btnCreateSignature.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Create Signature()");
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxIAgreeTo)
							.addGap(31)
							.addComponent(btnTermsOfService))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCopyrightStatement)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(34, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(351, Short.MAX_VALUE)
					.addComponent(btnCreateSignature)
					.addContainerGap())
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
					.addPreferredGap(ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
					.addComponent(btnCreateSignature)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
		try {
			uri = new URI("http://java.sun.com");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		
		
	
	}
	
	private void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { }
	    } else { throw new RuntimeException("No Desktop available to launch browser"); }
	}
	
	
	    class OpenUrlAction implements ActionListener {
	      @Override 
	      public void actionPerformed(ActionEvent e) {
	        open(uri);
	      }
	    }
}
