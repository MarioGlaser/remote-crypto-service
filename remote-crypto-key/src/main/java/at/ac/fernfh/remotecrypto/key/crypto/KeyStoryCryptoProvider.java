package at.ac.fernfh.remotecrypto.key.crypto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import at.ac.fernfh.remotecrypto.key.api.WrappingResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class KeyStoryCryptoProvider implements KeyCryptoProvider {

	private static final String PADDING = "RSA/ECB/PKCS1Padding";

	private Map<String, Triple> keyMap;
	
	abstract KeyStore getKeyStore();
	
	KeyStoryCryptoProvider() {
		this.keyMap = new HashMap<String, KeyStoryCryptoProvider.Triple>();
	}
	
	private byte[] wrapSecretKey(PublicKey publicKey, SecretKey secretKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {

		final Cipher cipher = Cipher.getInstance(PADDING);
		cipher.init(Cipher.WRAP_MODE, publicKey);

		return cipher.wrap(secretKey);
	}

	private SecretKey createSecretKey() {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}

		keyGen.init(256);
		return keyGen.generateKey();
	}

	private byte[] getEncrypteTestData(SecretKey secretKey, String testData) throws GeneralSecurityException {
		final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, new byte[12]));
		return cipher.doFinal(testData.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void addPublicKey(String alias, PublicKey publicKey, String testData) throws GeneralSecurityException {

		final SecretKey secretKey;
		if (keyMap.containsKey(alias)) {
			log.info("Alias already exists <{}>", alias);
			secretKey = (SecretKey) getKeyStore().getKey(alias, "changeit".toCharArray());
		} else {
			log.info("Create new Secret Key for alias <{}>", alias);
			secretKey = createSecretKey();

			getKeyStore().setEntry(alias, new SecretKeyEntry(secretKey), new KeyStore.PasswordProtection("changeit".toCharArray()));
			//secretKeyStore.setKeyEntry(alias, secretKey, "changeit".toCharArray(), null);

			try {
				getKeyStore().store(new FileOutputStream("./target/secret-key-store.jks"), "changeit".toCharArray());
			} catch (IOException e) {
				throw new IllegalStateException("Error saving the KeyStore", e);
			}
		}
		final byte[] wrappedSecretKey;
		final byte[] encryptedTestData;
		try {
			wrappedSecretKey = wrapSecretKey(publicKey, secretKey);
			encryptedTestData = getEncrypteTestData(secretKey, testData);
		} catch (GeneralSecurityException e) {
			throw new GeneralSecurityException("Could not create the wrapping key", e);
		}

		keyMap.put(alias, new Triple(publicKey, wrappedSecretKey, encryptedTestData));

	}

	@Override
	public WrappingResult getWrappingKey(String alias) {

		final Triple triple = keyMap.get(alias);
		return new WrappingResult(triple.getWrappedSecretKey(), triple.getEncryptedTestData());
	}

	public static class Triple {
		private PublicKey publicKey;
		private byte[] wrappedSecretKey;
		private byte[] encryptedTestData;

		public Triple(PublicKey publicKey, byte[] wrappedSecretKey, byte[] encryptedTestData) {
			super();
			this.publicKey = publicKey;
			this.wrappedSecretKey = wrappedSecretKey;
			this.encryptedTestData = encryptedTestData;
		}

		public PublicKey getPublicKey() {
			return publicKey;
		}

		public byte[] getWrappedSecretKey() {
			return wrappedSecretKey;
		}

		public byte[] getEncryptedTestData() {
			return encryptedTestData;
		}
	}
}
