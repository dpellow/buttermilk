package com.cryptoregistry.app;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JScrollPane;

import asia.redact.bracket.properties.Properties;

public class KeyMaterialsPanel extends JPanel {

	JTextPane textPane;
	
	private static final long serialVersionUID = 1L;

	public KeyMaterialsPanel(Properties props) {
		super();
		setLayout(new GridLayout(1, 0, 0, 0));
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(new Font("Courier", Font.PLAIN, 10));
		textPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		
		textPane.setText("Resultant Key Materials will be displayed here");
	}

	public JTextPane getTextPane() {
		return textPane;
	}


}
