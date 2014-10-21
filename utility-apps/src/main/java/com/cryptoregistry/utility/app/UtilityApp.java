package com.cryptoregistry.utility.app;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.cryptoregistry.client.security.SecurityManagerInterface;
import com.cryptoregistry.client.security.TrivialSecurityManager;
import com.cryptoregistry.client.security.TwoFactorSecurityManager;
import com.cryptoregistry.client.storage.DataStore;
import com.cryptoregistry.util.CmdLineParser;
import com.cryptoregistry.util.CmdLineParser.Option;
import com.cryptoregistry.util.CmdLineParser.OptionException;

import asia.redact.bracket.properties.Properties;
import asia.redact.bracket.util.Logger;

/**
 * Utility to perform initialization, validation of config, edit config, etc. 
 * 
 * @author Dave
 *
 */
public class UtilityApp {
	
	private static final Properties resBundle = Properties.Factory.getInstance("UtilityApp", Locale.ENGLISH);
	private Display display;
	private Shell shell;
	
	// app state
	String lastPath; // used to seed the file dialog
	String currentName; // the current file or URL name
	String fileName; // the current file
	
	Properties properties;
	SecurityManagerInterface securityManager;
	DataStore ds;
	
	static final String[] OPEN_FILTER_EXTENSIONS = new String[] {
		"*.json;*.js" };
	static final String[] OPEN_FILTER_NAMES = new String[] {
		resBundle.get("menu.file.import.filter.filetypes") + " (json)"};
	
	private String configFileArg = null;
	
	public UtilityApp(String [] args) {
		CmdLineParser parser = new CmdLineParser();
		Option<String> fileOpt = parser.addStringOption('f', "configFile");
		try {
			parser.parse(args);
			configFileArg = parser.getOptionValue(fileOpt,null);
		} catch (OptionException e) {
			e.printStackTrace();
		}
	}

	public static void main(String [] args) {
		Logger.get().trace();
		Logger.get().start();
		
		Display display = new Display();
		UtilityApp app = new UtilityApp(args);
		Shell shell = app.open(display);
		while (!shell.isDisposed()){
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	
	Shell open(Display _display) {
		
		Logger.get().trace("Creating the shell...");
		
		this.display = _display;
		shell = new Shell(display);
		shell.setText(resBundle.get("shell.title"));
		
		// Hook resize and dispose listeners.
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				//resizeShell(event);
			}
		});
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				if(ds != null) {
					ds.closeDb();
					Logger.get().info("Closed Datastore...");
				}
				Logger.get().info("UtilityApp is now closed");
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				
			}
		});
		
		configureDatastore();
		
		// Add a menu bar and widgets.
		createMenuBar();
		shell.pack();
		
		// Open the window
		shell.open();
		return shell;
	}
	
	boolean configureDatastore() {
	
		if(configFileArg != null){
			Logger.get().trace("configFileArg != null");
			File config = new File(configFileArg);
			if (!config.exists()) {
				return false;
			}else{
				properties = Properties.Factory.getInstance(config,Charset.forName("UTF-8"));
			}
		}else{
			return false;
		}
		
			String secModel = properties.get("security.model");
			if(secModel.trim().toLowerCase().contains("trivial")){
				securityManager = new TrivialSecurityManager(properties);
			}else if(secModel.trim().toLowerCase().contains("twofactor")){
				securityManager = new TwoFactorSecurityManager(properties);
			}else{
				throw new RuntimeException("Could not identify security model: "+secModel);
			}
		
			// checks
		if(!securityManager.keysExist()) {
			Logger.get().warn("Keys don't exist!");
			return false;
		}
		
		// TODO validate password or ask for it
		
		ds = new DataStore(properties,securityManager);
		Logger.get().trace("Datastore created: "+ds);
		Logger.get().trace("SecureMap size: "+ds.getViews().getSecureMap().size());
		return true;
			
	}
	
	Menu createMenuBar() {
		// Menu bar.
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		createFileMenu(menuBar);
		createDataStoreMenu(menuBar);
		createKeysMenu(menuBar);
		return menuBar;
	}
	
	void createDataStoreMenu(Menu menuBar){
		// Data Store menu
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText(resBundle.get("menu.datastore"));
		Menu dsMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(dsMenu);

		// Data Store -> Create Data Store...
		item = new MenuItem(dsMenu, SWT.PUSH);
		item.setText(resBundle.get("menu.datastore.create"));
		item.setAccelerator(SWT.MOD1 + 'C');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuCreateDataStore();
			}
		});
	}
	
	void createKeysMenu(Menu menuBar){
		// Data Store menu
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText(resBundle.get("menu.keys"));
		Menu dsMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(dsMenu);

		// Data Store -> Create Data Store...
		item = new MenuItem(dsMenu, SWT.PUSH);
		item.setText(resBundle.get("menu.keys.create"));
		item.setAccelerator(SWT.MOD1 + 'K');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuCreateKeys();
			}
		});
	}
	
	void menuCreateDataStore() {
		System.err.println("Create Datastore...");
	}
	
	void menuCreateKeys() {
		System.err.println("Create Keys...");
	}
	
	void createFileMenu(Menu menuBar) {
		
		// File menu
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText(resBundle.get("menu.file"));
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(fileMenu);

		// File -> Import File...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(resBundle.get("menu.file.import"));
		item.setAccelerator(SWT.MOD1 + 'I');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuImportFile();
			}
		});
		
		// File -> Open URL...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(resBundle.get("menu.file.importurl"));
		item.setAccelerator(SWT.MOD1 + 'U');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuOpenURL();
			}
		});
		
		/**
		 * 
		// File -> Reopen
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Reopen"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuReopen();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);

		// File -> Load File... (natively)
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("LoadFile"));
		item.setAccelerator(SWT.MOD1 + 'L');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuLoad();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		// File -> Save
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Save"));
		item.setAccelerator(SWT.MOD1 + 'S');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuSave();
			}
		});
		
		// File -> Save As...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Save_as"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuSaveAs();
			}
		});
		
		// File -> Save Mask As...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Save_mask_as"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuSaveMaskAs();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		// File -> Print
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Print"));
		item.setAccelerator(SWT.MOD1 + 'P');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuPrint();
			}
		});
		
		*/
		
		new MenuItem(fileMenu, SWT.SEPARATOR);

		// File -> Exit
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(resBundle.get("menu.file.exit"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
				try {
					Logger.get().shutDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	void menuOpenURL() {
		// Get the user to enter a URL.
		TextPrompter textPrompter = new TextPrompter(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		textPrompter.setText(resBundle.get("dialog.title"));
		textPrompter.setMessage(resBundle.get("dialog.msg"));
		String urlname = textPrompter.open();
		if (urlname == null) return;
		System.err.println(urlname);
	}
	
	void menuImportFile() {
		
		// Get the user to choose an image file.
		FileDialog fileChooser = new FileDialog(shell, SWT.OPEN);
		if (lastPath != null) {
			fileChooser.setFilterPath(lastPath);
		}
		fileChooser.setFilterExtensions(OPEN_FILTER_EXTENSIONS);
		fileChooser.setFilterNames(OPEN_FILTER_NAMES);
		String filename = fileChooser.open();
		lastPath = fileChooser.getFilterPath();
		if (filename == null)
			return;
		
		// do something with file chosen:
		System.err.println(filename);
	
	}
	
	class TextPrompter extends Dialog {
		String message = "";
		String result = null;
		Shell dialog;
		Text text;
		public TextPrompter (Shell parent, int style) {
			super (parent, style);
		}
		public TextPrompter (Shell parent) {
			this (parent, SWT.APPLICATION_MODAL);
		}
		public String getMessage () {
			return message;
		}
		public void setMessage (String string) {
			message = string;
		}
		public String open () {
			dialog = new Shell(getParent(), getStyle());
			dialog.setText(getText());
			dialog.setLayout(new GridLayout());
			Label label = new Label(dialog, SWT.NONE);
			label.setText(message);
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			text = new Text(dialog, SWT.SINGLE | SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = 300;
			text.setLayoutData(data);
			Composite buttons = new Composite(dialog, SWT.NONE);
			GridLayout grid = new GridLayout();
			grid.numColumns = 2;
			buttons.setLayout(grid);
			buttons.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			Button ok = new Button(buttons, SWT.PUSH);
			ok.setText(resBundle.get("dialog.button.ok"));
			data = new GridData();
			data.widthHint = 75;
			ok.setLayoutData(data);
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					result = text.getText();
					dialog.dispose();
				}
			});
			Button cancel = new Button(buttons, SWT.PUSH);
			cancel.setText(resBundle.get("dialog.button.cancel"));
			data = new GridData();
			data.widthHint = 75;
			cancel.setLayoutData(data);
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					dialog.dispose();
				}
			});
			dialog.setDefaultButton(ok);
			dialog.pack();
			dialog.open();
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep();
			}
			return result;
		}
	}


}
