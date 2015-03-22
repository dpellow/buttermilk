package com.cryptoregistry.mbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.apache.log4j.Logger;

import com.cryptoregistry.Buttermilk;
import com.cryptoregistry.client.storage.BDBDatastore;
import com.cryptoregistry.client.storage.SimpleKeyManager;
import com.cryptoregistry.formats.JSONFormatter;
import com.cryptoregistry.symmetric.SymmetricKeyContents;

import asia.redact.bracket.properties.OutputAdapter;
import asia.redact.bracket.properties.PlainOutputFormat;
import asia.redact.bracket.properties.Properties;

public class DatastoreManager extends StandardMBean implements
		DatastoreManagerMBean {

	final static Logger log = Logger.getLogger("com.cryptoregistry.mbean.DatastoreManager");

	private String buttermilkHome;
	private boolean buttermilkHomeIsValid;
	private boolean propsExists;

	private SimpleKeyManager keyManager;
	private BDBDatastore ds;

	public DatastoreManager() throws NotCompliantMBeanException {
		super(DatastoreManagerMBean.class);

		buttermilkHome = System.getenv("BUTTERMILK_HOME");
		File f = new File(buttermilkHome);
		if (!f.exists() && f.isDirectory()) {
			buttermilkHomeIsValid = false;
			log.error("BUTTERMILK_HOME is not defined or is not a directory: "
					+ buttermilkHome);
		} else {
			buttermilkHomeIsValid = true;
			log.info("BUTTERMILK_HOME is defined: " + buttermilkHome);
		}

		if (!buttermilkHomeIsValid)
			return;

		File props = new File(f, "buttermilk.properties");
		if (!props.exists()) {
			propsExists = false;
			log.info("buttermilk.properties is not present, you need to call createDatastore()");

		} else {
			propsExists = true;
			log.info("buttermilk.properties is defined, attempting to load datastore");
		}

		if (!propsExists)
			return;

		// load if things look good

		keyManager = new SimpleKeyManager();
		ds = new BDBDatastore(keyManager);

		log.info("loaded datastore");

	}

	public void createDatastore(String regHandle, String pathToPasswordFile) {

		if (!buttermilkHomeIsValid) {
			log.error("BUTTERMILK_HOME is not set.");
			return;
		}

		if (ds != null) {
			log.error("ds already seems to exist at the location specified by the BUTTERMILK_HOME env. variable.");
			return;
		}

		// ok, create the buttermilk.properties file

		File passwordProps = new File(pathToPasswordFile);
		if (!passwordProps.exists()) {
			log.error("pathToPasswordFile not found:" + pathToPasswordFile);
			return;
		}

		Properties props = Properties.Factory.getInstance(passwordProps,
				Charset.forName("UTF-8"));
		props.put("key.filename", "symmetric-key.json");
		props.put("buttermilk.datastore.home", "db");
		props.put("registrationKey.default", regHandle);
		props.put("registry.url", "http://www.cryptoregistry.com/");

		File bHome = new File(buttermilkHome);
		File bProps = new File(bHome, "buttermilk.properties");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(bProps);
			OutputAdapter adapter = new OutputAdapter(props);
			adapter.writeTo(out, new PlainOutputFormat(),
					Charset.forName("UTF-8"));
		} catch (FileNotFoundException e) {
			log.error(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		// create the symmetric key and write it out

		SymmetricKeyContents contents = Buttermilk.INSTANCE
				.generateSymmetricKey();
		JSONFormatter formatter = new JSONFormatter(regHandle);
		formatter.add(contents);

		File symmetricKeyFile = new File(bHome, props.get("key.filename"));
		try {
			out = new FileOutputStream(symmetricKeyFile);
			Writer writer = new OutputStreamWriter(out);
			formatter.format(writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		// create the data directory

		File db = new File(bHome, props.get("buttermilk.datastore.home"));
		db.mkdirs();

		keyManager = new SimpleKeyManager();
		ds = new BDBDatastore(keyManager);

		log.info("created datastore: " + ds);

	}

	protected String getDescription(MBeanInfo info) {
		return "Allow Buttermilk Datastores to be created and managed on client systems.";
	}

	protected String getParameterName(MBeanOperationInfo op, MBeanParameterInfo param, int sequence) {
		if (op.getName().equals("createDatastore")) {
			switch (sequence) {
			case 0:
				return "registrationHandle";
			case 1:
				return "pathToObfuscatedPasswordFile";
			default:
				return null;
			}
		}
		return null;
	}
	
	

}
