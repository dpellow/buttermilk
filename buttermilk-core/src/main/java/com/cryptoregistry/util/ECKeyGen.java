package com.cryptoregistry.util;

import java.io.Console;
import java.io.StringWriter;
import java.util.Arrays;

import com.cryptoregistry.ec.CryptoFactory;
import com.cryptoregistry.ec.ECKeyContents;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.formats.KeyFormat;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;

/**
 * Simple program to generate an Elliptic Curve Key and write it to file
 * 
 * @author Dave
 *
 */
public class ECKeyGen {

	private char[] password;
	private String regHandle, fileName;
	private boolean secured, unsecured, forPublication;
	private String curveName;
	
	
	
	public ECKeyGen(String curveName, String fileName, String regHandle, boolean secured, boolean unsecured, boolean forPublication) {
		super();
		this.fileName = fileName;
		this.regHandle = regHandle;
		this.secured = secured;
		this.unsecured = unsecured;
		this.forPublication = forPublication;
		this.curveName = curveName;
	}

	private void run() {
		
		Console console = System.console();
		console.format("%s\n\n", "...Create Elliptic Curve Key...");

		if(curveName == null || curveName.equals("")){
			collectCurveName(console);
		}
		if(regHandle == null || regHandle.equals("")){
			collectRegHandle(console);
		}
		if(secured){
			collectPassword(console);
		}

		ECKeyContents keys0 = CryptoFactory.INSTANCE.generateKeys(curveName);
		JSONFormatter format = new JSONFormatter(regHandle);
		if(unsecured)format.add(keys0); // formats an unsecured key
		if(secured) format.add(keys0.clone(new KeyFormat(password))); // formats a secured clone of the key with a Base64url encoding hint, which is right for Curve25519
		if(forPublication) format.add(keys0.cloneForPublication()); // makes a clone ready for publication	
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
	
	private void collectCurveName(Console console) {
		curveName = console.readLine("%s", "Enter CurveName:");
		if (curveName == null || curveName.equals("")) {
			console.format("%s\n", "Need a Curve!");
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
		Option<Boolean> publicOpt = parser.addBooleanOption('p', "forPublication");
		Option<Boolean> unsecuredOpt = parser.addBooleanOption('u', "unsecured");
		Option<Boolean> securedOpt = parser.addBooleanOption('s', "secured");
		Option<String> regHandleOpt = parser.addStringOption('r', "regHandle");
		Option<String> fileNameOpt = parser.addStringOption('f', "fileName");
		Option<String> curveNameOpt = parser.addStringOption('c', "curveName");
		try {
			parser.parse(args);
		} catch (OptionException e) {
			e.printStackTrace();
		}
		
		boolean includeForPublication = parser.getOptionValue(publicOpt, false);
		boolean includeUnsecured =  parser.getOptionValue(unsecuredOpt, false);
		boolean includeSecured =  parser.getOptionValue(securedOpt, true);
		String handle =  parser.getOptionValue(regHandleOpt,"");
		String fileName =  parser.getOptionValue(fileNameOpt,"ec-key.json");
		String curveName =  parser.getOptionValue(curveNameOpt,"P-256");
		ECKeyGen gen = new ECKeyGen(curveName, fileName, handle, includeSecured, includeUnsecured, includeForPublication);
		gen.run();
	}
}
