package com.cryptoregistry.app;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.KeyGenerationAlgorithm;

public class RegHandlePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPasswordField regHandleTextField;
	
	
	public RegHandlePanel(Properties props){
		super();
		
		final String regUrl = props.get("registration.url");
		
		JLabel lblRegistrationHandle = new JLabel("Registration Handle");
		
		regHandleTextField = new JPasswordField("");
		regHandleTextField.setColumns(10);
		
		JButton btnCheckAvailability = new JButton("Check Availability");
		btnCheckAvailability.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// use regUrl to check handle status
			}
		});
		
		JLabel lblAvailable = new JLabel("...");
		
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
											.addGap(200)
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
							.addComponent(lblAvailable))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addComponent(btnCheckAvailability)))
					.addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}


	public JPasswordField getRegHandleTextField() {
		return regHandleTextField;
	}
	
	
}
