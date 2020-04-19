package at.ac.fernfh.remotecrypto.key.api;

import lombok.Data;

/**
 * <b>Key Installation</b> request object.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@Data
public class KeyInstallationRequest {

	/** The public key for the user to install. */
	private byte[] publicKey;
}
