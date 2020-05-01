package at.ac.fernfh.remotecrypto.key.crypto;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import at.ac.fernfh.remotecrypto.key.crypto.config.KeyInstallationConfiguration;

@Component
public class CryptoProvider {

	@Autowired
	private KeyInstallationConfiguration keyInstallConfiguration;

	@Autowired
	private PKCS11CryptoProvider pkcs11CryptoProvider;
	
	public KeyCryptoProvider createCryptoProvider() {

		if (keyInstallConfiguration.getProvider().equalsIgnoreCase("PKCS11")) {
			pkcs11CryptoProvider.initialize();
			
			return pkcs11CryptoProvider;
		} else {
			throw new IllegalStateException(MessageFormat.format("Could not find provider for name <{0}>",
					keyInstallConfiguration.getProvider()));
		}
	}
}
