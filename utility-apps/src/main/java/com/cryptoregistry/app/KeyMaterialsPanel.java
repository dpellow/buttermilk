/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JScrollPane;

import asia.redact.bracket.properties.Properties;

public class KeyMaterialsPanel extends JPanel {

	JTextPane requestTextPane;
	JTextPane secureKeyTextPane;
	
	private static final long serialVersionUID = 1L;

	public KeyMaterialsPanel(Properties props) {
		super();
		setLayout(new GridLayout(1, 1, 0, 0));
		
		JPanel panel0 = new JPanel();
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.PAGE_AXIS));
		JLabel label0 = new JLabel("request.json.txt");
		requestTextPane = new JTextPane();
		requestTextPane.setEditable(false);
		requestTextPane.setFont(new Font("Courier", Font.PLAIN, 10));
		panel0.add(label0);
		panel0.add(requestTextPane);
	
		JScrollPane scrollPane0 = new JScrollPane(panel0);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
		JLabel label1 = new JLabel("secureKey.json.txt");
		secureKeyTextPane = new JTextPane();
		secureKeyTextPane.setEditable(false);
		secureKeyTextPane.setFont(new Font("Courier", Font.PLAIN, 10));
		panel1.add(label1);
		panel1.add(secureKeyTextPane);
		JScrollPane scrollPane1 = new JScrollPane(panel1);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane0, scrollPane1);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(350);
			splitPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		
		add(splitPane);
		
		requestTextPane.setText("Request will be displayed here");
		secureKeyTextPane.setText("Secure Key will be displayed here");
	}

	public JTextPane getRequestTextPane() {
		return requestTextPane;
	}

	public JTextPane getSecureKeyTextPane() {
		return secureKeyTextPane;
	}

}
