package com.cryptoregistry.app;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.swing.JComboBox;

import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.KeyGenerationAlgorithm;
import com.cryptoregistry.pbe.PBEAlg;
import com.cryptoregistry.util.Check10K;
import com.cryptoregistry.util.entropy.TresBiEntropy;
import com.cryptoregistry.util.entropy.TresBiEntropy.Result;
import javax.swing.JCheckBox;

public class CreateKeyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPasswordField password0;
	private JPasswordField password1;
	JLabel passwordEqualityMsg;
	JLabel againLbl;
	JLabel lblEntropy;
	JButton btnCreate;
	
	private Check10K tenK;
	
	
	public CreateKeyPanel(Properties props){
		super();
		
		tenK = new Check10K();
		
		JLabel lblPassword = new JLabel("Password");
		JLabel lblAgain = new JLabel("Again");
		
		password0 = new JPasswordField("");
		password0.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkPassword();
				checkEntropy();
			}
		});
		password0.setColumns(10);
		password1 = new JPasswordField("");
		password1.setColumns(10);
		password1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				checkPassword();
				checkEntropy();
			}
		});
		
		lblEntropy = new JLabel("Entropy: 0 bits");
		
		btnCreate = new JButton("Create Key");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createKey();
			}
		});
		
		KeyGenerationAlgorithm [] e = KeyGenerationAlgorithm.usableForSignature();
		JComboBox<KeyGenerationAlgorithm> comboBox = new JComboBox<KeyGenerationAlgorithm>();
		DefaultComboBoxModel<KeyGenerationAlgorithm> model = new DefaultComboBoxModel<KeyGenerationAlgorithm>(e);
		comboBox.setModel(model);
		
		PBEAlg [] pbes = PBEAlg.values();
		DefaultComboBoxModel<PBEAlg> pbemodel = new DefaultComboBoxModel<PBEAlg>(pbes);
		JComboBox<PBEAlg> comboBox_1 = new JComboBox<PBEAlg>();
		comboBox_1.setModel(pbemodel);
		
		JLabel lblKeyAlg = new JLabel("Asymmetric Key Algorithm");
		
		againLbl = new JLabel("");
		
		JLabel lblPasswordBasedEncryption = new JLabel("Password Based Encryption Algorithm");
		
		passwordEqualityMsg = new JLabel("...");
		
		JCheckBox chckbxCreateObfuscatedPassword = new JCheckBox("Create obfuscated password file from this value");
		
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(353, Short.MAX_VALUE)
					.addComponent(btnCreate)
					.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxCreateObfuscatedPassword)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblKeyAlg)
								.addComponent(againLbl)
								.addComponent(lblPasswordBasedEncryption))
							.addGap(31)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPassword)
								.addComponent(lblAgain))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addComponent(passwordEqualityMsg)
									.addGap(108)
									.addComponent(lblEntropy))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addComponent(password1)
									.addComponent(password0, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)))))
					.addContainerGap(45, Short.MAX_VALUE))
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
						.addComponent(againLbl)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblPasswordBasedEncryption)
							.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPassword)
						.addComponent(password0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAgain)
						.addComponent(password1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEntropy)
						.addComponent(passwordEqualityMsg))
					.addGap(28)
					.addComponent(chckbxCreateObfuscatedPassword)
					.addPreferredGap(ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
					.addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		setLayout(groupLayout);
		
	}


	public JPasswordField getPassword0() {
		return password0;
	}
	
	private void checkPassword() {
		char[]pass0 = password0.getPassword();
		char[]pass1 = password1.getPassword();
		
		if(pass0 == null || pass0.length==0){
			this.passwordEqualityMsg.setForeground(Color.RED);
			this.passwordEqualityMsg.setText("Empty");
			return;
		}
		
		boolean ok = Arrays.equals(pass0, pass1);
		if(ok) {
			this.passwordEqualityMsg.setForeground(Color.BLACK);
			this.passwordEqualityMsg.setText("Match");
			this.btnCreate.setEnabled(true);
			if(tenK.contains(new String(pass0))){
				this.passwordEqualityMsg.setForeground(Color.RED);
				this.passwordEqualityMsg.setText("In 10K List!");
				this.btnCreate.setEnabled(false);
			}
		}else{
			this.passwordEqualityMsg.setForeground(Color.RED);
			this.passwordEqualityMsg.setText("Input not equal");
			this.btnCreate.setEnabled(false);
		}
	}
	
	private void checkEntropy() {
		char[]pass0 = password0.getPassword();
		if(pass0 == null || pass0.length ==0) {
			this.lblEntropy.setText("Entropy: 0 bits");
			return;
		}
		byte [] bytes = toBytes(pass0);
		TresBiEntropy bi = new TresBiEntropy(bytes);
		Result res = bi.calc();
		int entropy = (int) res.bitsOfEntropy;
		this.lblEntropy.setText("Entropy: "+entropy+" bits");
	}
	
	private byte[] toBytes(char[] chars) {
	    CharBuffer charBuffer = CharBuffer.wrap(chars);
	    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
	            byteBuffer.position(), byteBuffer.limit());
	    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
	    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
	    return bytes;
	}
	
	private void createKey(){
		
	}
}
