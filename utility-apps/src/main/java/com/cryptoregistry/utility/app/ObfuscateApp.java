package com.cryptoregistry.utility.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import asia.redact.bracket.properties.Obfuscate;
import asia.redact.bracket.properties.OutputAdapter;
import asia.redact.bracket.properties.PlainOutputFormat;
import asia.redact.bracket.properties.Properties;

import com.cryptoregistry.passwords.SensitiveBytes;
import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;
import com.cryptoregistry.util.ShowHelpUtil;

/**
 * Obfuscate password files so they are harder to leak information. This is not
 * to be confused with encryption. Obfuscation just makes it harder for someone
 * to easily see the password if they have read access to the file.
 * 
 * This app will attempt to set the file permissions to read-only for the
 * current user.
 * 
 * @author Dave
 * 
 */
public class ObfuscateApp {

	public static final void main(String[] argv) {

		CmdLineParser parser = new CmdLineParser();
		Option<String> initOpt = parser.addStringOption('f', "file");

		try {
			parser.parse(argv);
		} catch (OptionException e) {
			e.printStackTrace();
		}

		String passwordFile = parser.getOptionValue(initOpt, null);
		if (passwordFile == null) {
			ShowHelpUtil.showHelp("/obfuscate-help-message.txt");
		} else {
			File f = new File(passwordFile);
			if (!f.exists()) {
				throw new RuntimeException("File does not exist: "
						+ passwordFile);
			}
			SensitiveBytes bytes;
			try {
				bytes = new SensitiveBytes(Files.readAllBytes(f.toPath()));
				bytes.trim();
				String passwordBase64 = Obfuscate.FACTORY.encrypt(bytes
						.getData());
				Properties props = Properties.Factory.getInstance();
				props.put("password", passwordBase64);
				OutputAdapter out = new OutputAdapter(props);
				out.writeTo(f, new PlainOutputFormat());
				f.setExecutable(false);
				f.setWritable(false);
				f.setReadable(true, true);
				System.out.println("Wrote password file to "
						+ f.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
