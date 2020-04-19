package at.ac.fernfh.remotecrypto.util;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Utility class.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
public final class SecHSMUtil {

	/**
	 * Get the <code>SHA-1</code> hash over the public key. 
	 * 
	 * @param publicKey the public key.
	 * 
	 * @return the calculated hash.
	 */
	public static byte[] getPublicKeyHash(final PublicKey publicKey) {
		Objects.requireNonNull(publicKey);

		final MessageDigest md;
		try {
			addBCProvider();
			md = MessageDigest.getInstance("SHA-1", BouncyCastleProvider.PROVIDER_NAME);
			return md.digest(publicKey.getEncoded());
		} catch (NoSuchProviderException | NoSuchAlgorithmException e) {
			throw new IllegalStateException("Cannot get the hash of the public key", e);
		}
	}

	/**
	 * Add the <code>BouncyCastleProvider</code> if not added before.
	 */
	public static void addBCProvider() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}

	/**
	 * Decode the passed public key.
	 * 
	 * @param publicKey the public key to decode.
	 * 
	 * @return the public key.
	 * 
	 * @throws GeneralSecurityException in case the public key could not be decoded/parsed.
	 */
	public static PublicKey parsePublicKey(byte[] publicKey) throws GeneralSecurityException {
		Objects.requireNonNull(publicKey);
		
		KeyFactory kf = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(publicKey);
		return (RSAPublicKey) kf.generatePublic(keySpecX509);
	}
}
