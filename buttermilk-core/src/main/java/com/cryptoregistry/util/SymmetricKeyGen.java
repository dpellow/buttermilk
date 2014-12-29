package com.cryptoregistry.util;

import java.io.Console;
import java.io.StringWriter;
import java.util.Arrays;

import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.symmetric.CryptoFactory;
import com.cryptoregistry.symmetric.SymmetricKeyContents;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;

/**
 * Simple program to generate a Symmetric Key and write it to file
 * 
 * @author Dave
 *
 */
public class SymmetricKeyGen {

	private char[] password;
	private String regHandle;
	
	public SymmetricKeyGen() {
		super();
	}
	
	public SymmetricKeyGen(char[] password, String regHandle) {
		super();
		this.password = password;
		this.regHandle = regHandle;
	}

	private void run() {
		
		final int keySize = 256; // bits
		
		String fileName ="symmetric-key.json";
		Console console = System.console();
		if(console == null) {
			// must rely on command-line input in this case
			
		}else{
			console.format("%s\n\n", "...Create Symmetric Key...");
			if(regHandle == null) collectRegHandle(console);
			if(password == null) collectPassword(console);
		}

		Password p = new NewPassword(password);
		SymmetricKeyContents keys0 = CryptoFactory.INSTANCE.generateKey(p, keySize);
		JSONFormatter format = new JSONFormatter(regHandle);
		format.add(keys0);
		StringWriter writer = new StringWriter();
		format.format(writer);
		FileUtil.writeFile(fileName, writer.toString());
		if(console != null) console.format("%s\n", "...Done...");
		p.selfDestruct();
		
	}

	private void collectRegHandle(Console console) {
		regHandle = console.readLine("%s", "Enter Registration Handle:");
		if (regHandle == null || regHandle.equals("")) {
			console.format("%s\n", "Need a registration handle!");
			System.exit(-2); // bail
		}
	}

	private void collectPassword(Console console) {
		char[] password0, password1 = null;
		password0 = console.readPassword("%s", "Passwd:");
		password1 = console.readPassword("%s", "Again:");
		if (Arrays.equals(password0, password1)) {
			password = password0;
			console.format("%s\n", "Password values match. Good.");
			return;
		} else {
			console.format("%s\n", "Password values did not match!");
			System.exit(-2); // bail
		}
	}
	
	public static void main(String[] args) {
		CmdLineParser parser = new CmdLineParser();
		Option<String> regHandleOpt = parser.addStringOption('r', "regHandle");
		Option<String> passwordOpt = parser.addStringOption('p', "password");
		try {
			parser.parse(args);
		} catch (OptionException e) {
			e.printStackTrace();
		}
		
		String regHandle =  parser.getOptionValue(regHandleOpt,null);
		String password =  parser.getOptionValue(passwordOpt,null);
		
		SymmetricKeyGen gen = new SymmetricKeyGen(password.toCharArray(),regHandle);
		gen.run();
	}
}
