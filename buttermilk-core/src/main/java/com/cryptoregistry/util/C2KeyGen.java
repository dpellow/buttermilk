package com.cryptoregistry.util;

import java.io.Console;
import java.io.StringWriter;
import java.util.Arrays;

import com.cryptoregistry.c2.CryptoFactory;
import com.cryptoregistry.c2.key.C2KeyMetadata;
import com.cryptoregistry.c2.key.Curve25519KeyContents;
import com.cryptoregistry.formats.JSONFormatter;

/**
 * Simple program to generate a Curve25519 Key and write it to file
 * 
 * @author Dave
 *
 */
public class C2KeyGen {

	private char[] password;
	private String regHandle;
	
	private void run() {
		
		String fileName ="c2-key.json";
		Console console = System.console();
		console.format("%s\n\n", "...Create Curve25519 Key...");

		collectRegHandle(console);
		collectPassword(console);

		C2KeyMetadata meta = C2KeyMetadata.createSecurePBKDF2(password);
		Curve25519KeyContents keys0 = CryptoFactory.INSTANCE.generateKeys(meta);
		JSONFormatter format = new JSONFormatter(regHandle);
		format.add(keys0);
		StringWriter writer = new StringWriter();
		format.format(writer);
		FileUtil.writeFile(fileName, writer.toString());
		console.format("%s\n", "...Done...");
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
		C2KeyGen gen = new C2KeyGen();
		gen.run();
	}
}
