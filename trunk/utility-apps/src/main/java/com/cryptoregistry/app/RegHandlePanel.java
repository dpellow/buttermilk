package com.cryptoregistry.app;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.handle.CryptoHandle;
import com.cryptoregistry.handle.Handle;

public class RegHandlePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final JTextField regHandleTextField;
	private final RegHandleChecker checker;
	
	public RegHandlePanel(Properties props){
		super();
		checker = new RegHandleChecker(props);
	
		JLabel lblRegistrationHandle = new JLabel("Registration Handle");
		final JLabel validationLabel = new JLabel("...");
		final JLabel lblAvailable = new JLabel("...");
		
		regHandleTextField = new JTextField("");
		regHandleTextField.setColumns(10);
		
		JButton btnCheckAvailability = new JButton("Check Availability");
		btnCheckAvailability.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String regHandle = regHandleTextField.getText();
				if(regHandle == null || regHandle.trim().equals("")){
					// do nothing
				}
				boolean ok = checker.check(regHandle);
				if(ok) {
					lblAvailable.setText("Available!");
				}else{
					lblAvailable.setText("Not Available, Sorry.");
				}
			}
		});
		
		
		regHandleTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				String text = regHandleTextField.getText();
				Handle h = CryptoHandle.parseHandle(text);
				if(h.validate()){
					validationLabel.setText("Valid Syntax: "+h.getClass().getSimpleName());
				}else{
					validationLabel.setText("Format Error: "+h);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// do nothing
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//do nothing
				
			}
			
		});
		
		
		JButton btnCreate = new JButton("OK");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Calling create()");
			}
		});
		
		// the circumlocution here allows GUI builder tool to work
		KeyGenerationAlgorithm [] e = KeyGenerationAlgorithm.usableForSignature();
		DefaultComboBoxModel<KeyGenerationAlgorithm> model = new DefaultComboBoxModel<KeyGenerationAlgorithm>(e);
		
	
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblRegistrationHandle)
							.addContainerGap(346, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(regHandleTextField, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGap(10)
											.addComponent(validationLabel)
											.addGap(144)
											.addComponent(lblAvailable)
											.addPreferredGap(ComponentPlacement.RELATED, 124, Short.MAX_VALUE))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(btnCheckAvailability)
											.addGap(31)))
									.addGap(47)))
							.addGap(26))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCreate)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblRegistrationHandle)
					.addGap(10)
					.addComponent(regHandleTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblAvailable)
								.addComponent(validationLabel)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addComponent(btnCheckAvailability)))
					.addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}


	public JTextField getRegHandleTextField() {
		return regHandleTextField;
	}
}
