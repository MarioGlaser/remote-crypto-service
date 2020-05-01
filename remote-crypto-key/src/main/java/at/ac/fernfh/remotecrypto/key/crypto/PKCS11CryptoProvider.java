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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import at.ac.fernfh.remotecrypto.key.crypto.config.KeyInstallationConfiguration;
import lombok.extern.slf4j.Slf4j;
import sun.security.pkcs11.SunPKCS11;

/**
 * PKCS#11 implementation of the <code>KeyCryptoProvider</code>.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@SuppressWarnings("restriction")
@Slf4j
@Component
public class PKCS11CryptoProvider extends KeyStoryCryptoProvider implements KeyCryptoProvider {

	private String configurationPath;

	private KeyStore keyStore;

	private String pin = "1234";

	@Autowired
	private KeyInstallationConfiguration keyInstallConfiguration;

	@SuppressWarnings("resource")
	public PKCS11CryptoProvider() {


	}

	@Override
	KeyStore getKeyStore() {
		return keyStore;
	}

	@Override
	public void initialize() {
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
		configWriter.println("slotListIndex=" + keyInstallConfiguration.getSlot());
//		configWriter.println("slotListIndex=0");
		configWriter.println("showInfo=true");

		try {
			Provider provider = new SunPKCS11(tmpConfigFile.getAbsolutePath());
			Security.addProvider(provider);
			keyStore = KeyStore.getInstance("PKCS11", provider);
			keyStore.load(null, keyInstallConfiguration.getPassword().toCharArray());
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
}
