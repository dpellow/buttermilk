package com.cryptoregistry.proto.frame;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class just provides some reusable utility methods for the subclasses
 * 
 * @author Dave
 *
 */
public class InputFrameBase {
	
	// could possibly make use of a byte buffer here, but let's not pre-optimize

	public InputFrameBase() {
	}

	public final void readFully(InputStream in, byte b[], int off, int len)
			throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = in.read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}

	public final int readInt(InputStream in) throws IOException {
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

}
