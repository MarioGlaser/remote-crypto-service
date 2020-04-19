package at.ac.fernfh.remotecrypto.model;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.bouncycastle.util.encoders.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.ac.fernfh.remotecrypto.util.SecHSMUtil;
import lombok.Data;

/**
 * Domain object of an registered user.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@Entity
@Data
public class RegisteredUser {

	/** Time of authentication. */
	@Column
	private Instant authTime;

	/** Device information - free text. */
	@Column(length = 2048)
	private String deviceInfo;

	/** E-Mail adress. */
	@Column(length = 128)
	private String eMail;

	/** Encrpyted test data to verify the decryption.*/
	@Column(length = 4096)
	private String encryptedTestData;

	/** Encrypted wrapping key, only be stored in the database for convenience. */
	@Column(length = 4096)
	private String encryptedWrappingKey;

	/** Expiration time of the token. */
	@Column
	private Instant expTime;

	/** Internal ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ID token. */
	@Column(length = 2048)
	private String idToken;

	/** Registered public key. */
	@JsonIgnore
	@Transient
	private PublicKey publicKey;

	/** Hash of the public key. */
	@Column(length = 1024)
	@NotNull
	private String pulbicKeyHash;

	/** Registered public key. */
	@Column(length = 4096)
	@NotNull
	private String pulbicKeyValue;

	/** Subject (user). */
	@Column(length = 128)
	private String subject;

	/** Get the decoded public key. */
	public PublicKey getPublicKey() {
		if (publicKey == null && pulbicKeyValue != null) {
			try {
				publicKey = SecHSMUtil.parsePublicKey(Base64.decode(pulbicKeyValue));
			} catch (GeneralSecurityException e) {
				throw new IllegalStateException("Already stored public key should always in the correct format to parse", e);
			}
		}
		
		return publicKey;
	}
}

