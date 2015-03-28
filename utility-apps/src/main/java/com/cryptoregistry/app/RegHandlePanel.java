package com.cryptoregistry.app;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ExecutionException;

import asia.redact.bracket.properties.Properties;

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
				final String regHandle = regHandleTextField.getText();
				if(regHandle == null || regHandle.trim().equals("")){
					// do nothing
					return;
				}
				
				SwingWorker<Boolean,String> worker = new SwingWorker<Boolean,String>() {
					
					@Override
					protected Boolean doInBackground() throws Exception {
						return checker.check(regHandle);
					}
					
					 @Override
					public void done() {
						 try {
								if(get()) {
									lblAvailable.setText("Available!");
								}else{
									lblAvailable.setText("Not Available, Sorry.");
								}
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				};
				worker.execute();
			}
		});
		
		
		regHandleTextField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				lblAvailable.setText("...");
				String text = regHandleTextField.getText();
				Handle h = CryptoHandle.parseHandle(text);
				if(h.validate()){
					validationLabel.setText("Valid Syntax: type "+h.getClass().getSimpleName());
					validationLabel.setForeground(Color.BLACK);
				}else{
					validationLabel.setText("Formatting error.");
					validationLabel.setForeground(Color.RED);
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// keyTyped(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//do nothing
				
			}
			
		});
		
		JButton btnCreate = new JButton("OK");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingRegistrationWizardGUI.tabbedPane.setSelectedIndex(2);
			}
		});
		
		// the circumlocution here allows GUI builder tool to work
	//	KeyGenerationAlgorithm [] e = KeyGenerationAlgorithm.usableForSignature();
	//	DefaultComboBoxModel<KeyGenerationAlgorithm> model = new DefaultComboBoxModel<KeyGenerationAlgorithm>(e);
		
	
		
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
