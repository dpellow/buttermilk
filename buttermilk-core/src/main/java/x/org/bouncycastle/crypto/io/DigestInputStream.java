package x.org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import x.org.bouncycastle.crypto.Digest;

/**
 * This has been updated to support bTLS purposes
 * 
 * @author Dave
 *
 */

public class DigestInputStream extends FilterInputStream {
	protected Digest digest;

	public DigestInputStream(InputStream stream, Digest digest) {
		super(stream);
		this.digest = digest;
	}

	public int read() throws IOException {
		int b = in.read();

		if (b >= 0) {
			digest.update((byte) b);
		}
		return b;
	}

	public int read(byte[] b, int off, int len) throws IOException {
		int n = in.read(b, off, len);
		if (n > 0) {
			digest.update(b, off, n);
		}
		return n;
	}

	public int read(byte[] b) throws IOException {
		int n = in.read(b);
		if (n > 0) {
			digest.update(b, 0, n);
		}
		return n;
	}

	public byte[] getDigest() {
		byte[] res = new byte[digest.getDigestSize()];
		digest.doFinal(res, 0);
		return res;
	}
	
	public void reset() {
		digest.reset();
	}
}
