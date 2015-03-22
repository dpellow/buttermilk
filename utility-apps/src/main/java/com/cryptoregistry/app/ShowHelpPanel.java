package com.cryptoregistry.app;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JScrollPane;

import asia.redact.bracket.properties.Properties;

public class ShowHelpPanel extends JPanel {


	private static final long serialVersionUID = 1L;

	public ShowHelpPanel(Properties props) {
		super();
		setLayout(new GridLayout(1, 0, 0, 0));
		
		java.net.URL helpURL = this.getClass().getResource("/helptext.html");
		
		JTextPane textPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		
		try {
			textPane.setPage(helpURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
