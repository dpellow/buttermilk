package x.org.bouncycastle.crypto;

/**
 * this exception is thrown whenever a cipher requires a change of key, iv or
 * similar after x amount of bytes enciphered
 */
public class MaxBytesExceededException extends RuntimeCryptoException {

	private static final long serialVersionUID = 1L;

	/**
	 * base constructor.
	 */
	public MaxBytesExceededException() {
	}

	/**
	 * create an with the given message.
	 * 
	 * @param message
	 *            the message to be carried with the exception.
	 */
	public MaxBytesExceededException(String message) {
		super(message);
	}
}
