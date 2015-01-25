package x.org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import x.org.bouncycastle.crypto.Digest;

/**
 * This has been updated to support bTLS purposes
 * 
 * @author Dave
 *
 */
public class DigestOutputStream extends FilterOutputStream {
	protected Digest digest;

	public DigestOutputStream(OutputStream out, Digest digest) {
		super(out);
		this.digest = digest;
	}

	public void write(int b) throws IOException {
		digest.update((byte) b);
		out.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		digest.update(b, off, len);
		out.write(b, off, len);
	}

	public void write(byte[] b) throws IOException {
		digest.update(b, 0, b.length);
		out.write(b);
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
