package at.ac.fernfh.remotecrypto.registration.api;

import lombok.Data;

/**
 * Domain-Klasse die eine <b>Registrierung</b> des Benutzers repräsentiert.
 *  
 * @author Mario Glaser
 * @since 1.0
 */
@Data
public class Registration {

	/** Öffentlicher Schlüssel der Registrierung. */
	private byte[] publicKey;
		
	/** Geräte-Informationen. */
	private String deviceInfo;
}
