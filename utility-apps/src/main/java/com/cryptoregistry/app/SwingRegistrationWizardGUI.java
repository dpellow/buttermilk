package com.cryptoregistry.app;

import java.awt.Dimension;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import asia.redact.bracket.properties.Properties;

public class SwingRegistrationWizardGUI {
	
	 public static JTabbedPane tabbedPane;

	 private static void createAndShowGUI(Properties props) {
		 
		 try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e) {}
		 
	        //Create and set up the window.
	        JFrame frame = new JFrame("cryptoregistry.com - Registration Key Materials Wizard");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setPreferredSize(new Dimension(970,570));
	       
	        tabbedPane = new JTabbedPane();
	        tabbedPane.setPreferredSize(new Dimension(970,350));
	       
	        final ShowHelpPanel showHelpPanel = new ShowHelpPanel(props);
	        final SettingsPanel settingsPanel = new SettingsPanel(props);
	        final RegHandlePanel regHandlePanel = new RegHandlePanel(props);
	        final CreateKeyPanel createKeyPanel = new CreateKeyPanel(props);
	        final PersonalContactPanel personalContactPanel = new PersonalContactPanel();
	        final BusinessContactPanel businessContactPanel = new BusinessContactPanel();
	        final WebsiteContactPanel websiteContactPanel = new WebsiteContactPanel();
	        final SignaturePanel signaturePanel = new SignaturePanel();
	        final KeyMaterialsPanel keyMaterialsPanel = new KeyMaterialsPanel(props);
	        
	    	settingsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	regHandlePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	createKeyPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	personalContactPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	businessContactPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	websiteContactPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    	signaturePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	        
	        tabbedPane.addTab("Introduction", showHelpPanel);
	        tabbedPane.addTab("Settings", settingsPanel);
	        tabbedPane.addTab("Select Registration Handle", regHandlePanel);
	        tabbedPane.addTab("Initial Key", createKeyPanel);
	        tabbedPane.addTab("Personal Contact", personalContactPanel);
	        tabbedPane.addTab("Business Contact", businessContactPanel);
	        tabbedPane.addTab("Website Contact", websiteContactPanel);
	        tabbedPane.addTab("Signature", signaturePanel);
	        tabbedPane.addTab("Generated Key Materials", keyMaterialsPanel);
	        
	        ChangeListener changeListener = new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					 JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
				        int index = sourceTabbedPane.getSelectedIndex();
				        switch(index){
				        	case 0: {
				        	
				        		break;
				        	}
				        	case 1:{
				        		regHandlePanel.getRegHandleTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 2:{
				        		createKeyPanel.getPassword0().requestFocusInWindow();
				        		break;
				        	}
				        	case 3:{
				        		personalContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 4:{
				        		businessContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 5:{
				        		websiteContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 6:{
				        	//	websiteContactPanel.getTextField().requestFocusInWindow();
				        		break;
				        	}
				        	case 7:{
				        		keyMaterialsPanel.getTextPane().requestFocusInWindow();
				        		break;
				        	}
				        	default: {}
				        }
					
				}
	        };
	        
	        tabbedPane.addChangeListener(changeListener);
	        
	        JPanel outer = new JPanel();
	        outer.setLayout(new BoxLayout(outer, BoxLayout.PAGE_AXIS));
	        outer.add(tabbedPane);
	        
	        frame.getContentPane().add(outer);
	        
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	       // regHandlePanel.getRegHandleTextField().requestFocusInWindow();
	    }
	 
	    public static void main(String[] args) {
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	InputStream in = Thread.currentThread()
	            			.getContextClassLoader().getResourceAsStream("regwizard.properties");
	            	Properties props = Properties.Factory.getInstance(in);
	                createAndShowGUI(props);
	            }
	        });
	    }

}
