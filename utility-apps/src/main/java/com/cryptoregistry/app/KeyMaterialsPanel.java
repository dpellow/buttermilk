/*
 *  This file is part of Buttermilk
 *  Copyright 2011-2015 David R. Smith All Rights Reserved.
 *
 */
package com.cryptoregistry.app;

import javax.swing.BorderFactory;
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
		
		requestTextPane = new JTextPane();
		requestTextPane.setEditable(false);
		requestTextPane.setFont(new Font("Courier", Font.PLAIN, 10));
	//	requestTextPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JScrollPane scrollPane0 = new JScrollPane(requestTextPane);
		
		secureKeyTextPane = new JTextPane();
		secureKeyTextPane.setEditable(false);
		secureKeyTextPane.setFont(new Font("Courier", Font.PLAIN, 10));
		JScrollPane scrollPane1 = new JScrollPane(secureKeyTextPane);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane0, scrollPane1);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(150);
			splitPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		
		add(splitPane);
		
		requestTextPane.setText("Resultant Key Materials will be displayed here");
		secureKeyTextPane.setText("Secure Key will be displayed here");
	}

	public JTextPane getRequestTextPane() {
		return requestTextPane;
	}

	public JTextPane getSecureKeyTextPane() {
		return secureKeyTextPane;
	}

}
