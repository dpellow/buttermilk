package com.cryptoregistry.utility.app;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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

/**<p>
 * Obfuscate password files so they are harder to make leak information. This is not
 * to be confused with encryption. Obfuscation uses encryption technique, but just makes it harder for someone
 * to easily see the password if they have read access to the file.</p>
 * 
 * <p>To use this, put a plain text password in a file, then pass that file name in as -f <filename>. The
 * program will obfuscate the password in the file. The result will be in properties format where the key
 * is "password" and the value is the obfuscated value</p>
 * 
 * <p>The key used for this is compiled into the program, so it is portable. To make the obfuscation more
 * specific to your system, put a file called "salt.txt" into your home folder or into the
 * top level of the classpath for this program. Warning: this does make your program non-portable.</p>
 * 
 * <p>This app will attempt to set the file permissions to read-only for the current user.</p>
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
				throw new RuntimeException("File does not exist: "+ passwordFile);
			}
			SensitiveBytes bytes;
		
			try {
				
				// where our stuff goes
				File propFile = new File(f.getCanonicalPath()+".properties");
				
				bytes = new SensitiveBytes(Files.readAllBytes(f.toPath()));
				bytes.trim();
				String passwordBase64 = Obfuscate.FACTORY.encrypt(bytes.getData());
				Properties props = Properties.Factory.getInstance();
				props.put("password", passwordBase64);
				OutputAdapter out = new OutputAdapter(props);
				out.writeTo(propFile, new PlainOutputFormat(), Charset.forName("UTF-8"));
				f.setExecutable(false);
				f.setWritable(false);
				f.setReadable(true, true);
				System.out.println("Wrote password file to "+ propFile.getCanonicalPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
