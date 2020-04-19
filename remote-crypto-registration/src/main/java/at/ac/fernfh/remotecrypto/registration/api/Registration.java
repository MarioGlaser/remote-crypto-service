package at.ac.fernfh.remotecrypto.registration.api;

import lombok.Data;

/**
 * Domain-Class of the <b>Registration</b> - represents a user.
 *  
 * @author Mario Glaser
 * @since 1.0
 */
@Data
public class Registration {

	/** Public key of the registration. */
	private byte[] publicKey;
		
	/** Device informations. */
	private String deviceInfo;
}
