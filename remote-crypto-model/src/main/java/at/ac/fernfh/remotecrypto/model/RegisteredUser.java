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

@Entity
@Data
public class RegisteredUser {

	@Column
	private Instant authTime;

	@Column(length = 2048)
	private String deviceInfo;

	@Column(length = 128)
	private String eMail;

	@Column(length = 4096)
	private String encryptedTestData;

	@Column(length = 4096)
	private String encryptedWrappingKey;

	@Column
	private Instant expTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 2048)
	private String idToken;

	@JsonIgnore
	@Transient
	private PublicKey publicKey;

	@Column(length = 1024)
	@NotNull
	private String pulbicKeyHash;

	@Column(length = 4096)
	@NotNull
	private String pulbicKeyValue;

	@Column(length = 128)
	private String subject;

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

