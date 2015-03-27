package com.cryptoregistry.app;

import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import asia.redact.bracket.properties.Properties;

public class ShowHelpPanel extends JPanel {


	private static final long serialVersionUID = 1L;

	public ShowHelpPanel(Properties props) {
		super();
		setLayout(new GridLayout(1, 0, 0, 0));
		
		java.net.URL helpURL = this.getClass().getResource("/helptext.html");
		
		final JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textPane);
		add(scrollPane);
		
		textPane.addHyperlinkListener(new HyperlinkListener() {
	        public void hyperlinkUpdate(final HyperlinkEvent pE) {
	            if (HyperlinkEvent.EventType.ACTIVATED == pE.getEventType()) {
	                String desc = pE.getDescription();
	                if (desc == null || !desc.startsWith("#")) return;
	                desc = desc.substring(1);
	                textPane.scrollToReference(desc);
	            }
	        }
	    });
		
		try {
			textPane.setPage(helpURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
