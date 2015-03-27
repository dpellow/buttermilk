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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.pbe.PBEAlg;

public class CreateKeyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPasswordField textField_1;
	private JTextField textField_2;
	
	
	public CreateKeyPanel(Properties props){
		super();
		
		JLabel lblPassword = new JLabel("Password");
		JLabel lblAgain = new JLabel("Again");
		
		textField_1 = new JPasswordField("");
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				System.err.println("key released: "+e.getKeyChar());
			}
		});
		textField_1.setColumns(10);
		textField_2 = new JTextField("");
		textField_2.setColumns(10);
		
		JLabel lblEntropy = new JLabel("Entropy: 0 bits");
		
		JButton btnCreate = new JButton("Create Key");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.err.println("Calling create()");
			}
		});
		
		// the circumlocution here allows GUI builder tool to work
		KeyGenerationAlgorithm [] e = KeyGenerationAlgorithm.usableForSignature();
		JComboBox<KeyGenerationAlgorithm> comboBox = new JComboBox<KeyGenerationAlgorithm>();
		DefaultComboBoxModel<KeyGenerationAlgorithm> model = new DefaultComboBoxModel<KeyGenerationAlgorithm>(e);
		comboBox.setModel(model);
		comboBox.addActionListener(new ActionListener() {

			@SuppressWarnings("rawtypes")
			@Override
			public void actionPerformed(ActionEvent e) {
				System.err.println("Selected "+((JComboBox)e.getSource()).getSelectedItem());
				
			}
			
		});
		
		PBEAlg [] pbes = PBEAlg.values();
		DefaultComboBoxModel<PBEAlg> pbemodel = new DefaultComboBoxModel<PBEAlg>(pbes);
		JComboBox<PBEAlg> comboBox_1 = new JComboBox<PBEAlg>();
		comboBox_1.setModel(pbemodel);
		
		JLabel lblKeyAlg = new JLabel("Asymmetric Key Algorithm");
		
		JLabel label = new JLabel("");
		
		JLabel lblPasswordBasedEncryption = new JLabel("Password Based Encryption Algorithm");
		
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPassword)
								.addComponent(lblAgain))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textField_2)
								.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
							.addGap(45))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGap(228)
							.addComponent(lblEntropy)
							.addGap(141))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblKeyAlg)
								.addComponent(label)
								.addComponent(lblPasswordBasedEncryption))
							.addGap(31)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(145, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCreate)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblKeyAlg)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(label)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblPasswordBasedEncryption)
							.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAgain)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEntropy)
					.addPreferredGap(ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
					.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}


	public JPasswordField getTextField_1() {
		return textField_1;
	}
	
	
}
