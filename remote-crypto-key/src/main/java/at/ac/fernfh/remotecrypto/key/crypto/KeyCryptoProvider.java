package at.ac.fernfh.remotecrypto.key.crypto;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

import at.ac.fernfh.remotecrypto.key.api.WrappingResult;

public interface KeyCryptoProvider {
	
	void addPublicKey (String alias, PublicKey publicKey, String testData) throws GeneralSecurityException;
	
	WrappingResult getWrappingKey(String alias);
}
