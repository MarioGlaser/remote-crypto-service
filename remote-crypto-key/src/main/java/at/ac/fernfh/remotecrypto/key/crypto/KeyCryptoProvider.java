package at.ac.fernfh.remotecrypto.key.crypto;

import java.security.GeneralSecurityException;
import java.security.PublicKey;

import at.ac.fernfh.remotecrypto.key.api.KeyInstallationResult;

/**
 * Interface definition of the <b>security module implementation</b>.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
public interface KeyCryptoProvider {

	void initialize();
	
	/**
	 * Get the key installation result for the passed user (alias).
	 * 
	 * @param alias the username - to find the corresponding key in the security module.
	 * @return the key installation result.
	 */
	KeyInstallationResult getWrappingKey(String alias);

	/**
	 * Install a new public key for the passed user (alias).
	 * 
	 * @param alias     the alias - username.
	 * @param publicKey
	 * @param testData
	 * 
	 * @throws GeneralSecurityException in case an exception on the underlying
	 *                                  security module implementation occurred.
	 */
	void installPublicKey(String alias, PublicKey publicKey, String testData) throws GeneralSecurityException;
}
