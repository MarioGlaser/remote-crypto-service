package at.ac.fernfh.remotecrypto.key.api;

public class WrappingResult {

	
	private byte[] wrappedSecretKey;
	
	private byte[] encryptedTestData;

	public WrappingResult(byte[] wrappedSecretKey, byte[] encryptedTestData) {
		super();
		this.wrappedSecretKey = wrappedSecretKey;
		this.encryptedTestData = encryptedTestData;
	}
	
	public byte[] getEncryptedTestData() {
		return encryptedTestData;
	}
	
	public byte[] getWrappedSecretKey() {
		return wrappedSecretKey;
	}
}
