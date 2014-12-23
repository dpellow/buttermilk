package com.cryptoregistry.util;

import java.io.Console;
import java.io.StringWriter;
import java.util.Arrays;

import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;
import com.cryptoregistry.symmetric.CryptoFactory;
import com.cryptoregistry.symmetric.SymmetricKeyContents;

/**
 * Simple program to generate a Symmetric Key and write it to file
 * 
 * @author Dave
 *
 */
public class SymmetricKeyGen {

	private char[] password;
	private String regHandle;
	
	private void run() {
		
		final int keySize = 256;
		
		String fileName ="symmetric-key.json";
		Console console = System.console();
		console.format("%s\n\n", "...Create Symmetric Key...");

		collectRegHandle(console);
		collectPassword(console);

		Password p = new NewPassword(password);
		SymmetricKeyContents keys0 = CryptoFactory.INSTANCE.generateKey(keySize, p);
		JSONFormatter format = new JSONFormatter(regHandle);
		format.add(keys0);
		StringWriter writer = new StringWriter();
		format.format(writer);
		FileUtil.writeFile(fileName, writer.toString());
		console.format("%s\n", "...Done...");
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
		SymmetricKeyGen gen = new SymmetricKeyGen();
		gen.run();
	}
}
