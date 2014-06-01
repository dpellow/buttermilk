package com.cryptoregistry.formats;

import com.cryptoregistry.passwords.NewPassword;
import com.cryptoregistry.passwords.Password;

/**
 * Encapsulate various modes for formatting
 * 
 * @author Dave
 *
 */
public class KeyFormat {

	public final Encoding encoding;
	public final Mode mode;
	public final Password password;
	
	public KeyFormat() {
		encoding = Encoding.Base64url;
		mode = Mode.OPEN;
		password = null;
	}
	
	public KeyFormat(Mode mode) {
		encoding = Encoding.Base64url;
		this.mode = mode;
		password = null;
	}
	
	public KeyFormat(char [] password) {
		encoding = Encoding.Base64url;
		mode = Mode.SEALED;
		this.password = new NewPassword(password);
	}

	public KeyFormat(Encoding encoding, Mode mode,
			Password password) {
		super();
		this.encoding = encoding;
		this.mode = mode;
		this.password = password;
	}

}
