package at.ac.fernfh.remotecrypto.key.api;

/**
 * <b>Key Installation</b> response object.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
public class KeyInstallationResult {

	/** Encrypted test data for the client, encrypted with the <code>wrappedSecretKey</code>. */
	private byte[] encryptedTestData;
	
	/** The secret key to install on the client. */
	private byte[] wrappedSecretKey;

	/**
	 * Create new installation response object with the passed parameters. 
	 * 
	 * @param wrappedSecretKey the wrapped secret key.
	 * @param encryptedTestData the encrypted test data.
	 */
	public KeyInstallationResult(byte[] wrappedSecretKey, byte[] encryptedTestData) {
		super();
		this.wrappedSecretKey = wrappedSecretKey;
		this.encryptedTestData = encryptedTestData;
	}
	
	/**
	 * 
	 * @return the encrypted test data.
	 */
	public byte[] getEncryptedTestData() {
		return encryptedTestData;
	}

	/**
	 * 
	 * @return the wrapped secret key.
	 */
	public byte[] getWrappedSecretKey() {
		return wrappedSecretKey;
	}
}
