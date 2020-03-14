package at.ac.fernfh.remotecrypto.key.crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;
import sun.security.pkcs11.SunPKCS11;

@Slf4j
public class PKCS11CryptoProvider extends KeyStoryCryptoProvider implements KeyCryptoProvider {

	private String pin;

	private String configurationPath;

	private KeyStore keyStore;

	public PKCS11CryptoProvider() {

		File tmpConfigFile;
		try {
			tmpConfigFile = File.createTempFile("pkcs11-", "conf");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		tmpConfigFile.deleteOnExit();
		PrintWriter configWriter;
		try {
			configWriter = new PrintWriter(new FileOutputStream(tmpConfigFile), true);
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
		configWriter.println("name=secure-hsm");
		configWriter.println("library=/usr/local/lib/softhsm/libsofthsm2.so");
		configWriter.println("slotListIndex=1");
//		configWriter.println("slotListIndex=0");
		configWriter.println("showInfo=true");

		try {
			Provider provider = new SunPKCS11(tmpConfigFile.getAbsolutePath());
			Security.addProvider(provider);
			keyStore = KeyStore.getInstance("PKCS11", provider);
			keyStore.load(null, "1234".toCharArray());
		} catch (GeneralSecurityException | IOException e) {
			throw new IllegalStateException(e);
		}

		Enumeration<String> aliases;
		try {
			aliases = keyStore.aliases();
		} catch (KeyStoreException e) {
			throw new IllegalStateException(e);
		}

		while (aliases.hasMoreElements()) {
			log.info("Alias: " + aliases.nextElement());
		}
	}

	@Override
	KeyStore getKeyStore() {
		return keyStore;
	}
}
