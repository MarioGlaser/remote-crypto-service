package at.ac.fernfh.remotecrypto.key.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import at.ac.fernfh.remotecrypto.key.api.KeyInstallationResult;
import at.ac.fernfh.remotecrypto.key.crypto.CryptoProvider;
import at.ac.fernfh.remotecrypto.key.crypto.KeyCryptoProvider;
import at.ac.fernfh.remotecrypto.key.crypto.PKCS11CryptoProvider;
import at.ac.fernfh.remotecrypto.key.crypto.SoftwareCryptoProvider;
import at.ac.fernfh.remotecrypto.model.RegisteredUser;
import at.ac.fernfh.remotecrypto.model.repository.UserRepository;
import at.ac.fernfh.remotecrypto.util.SecHSMUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation of the <b>Key Installation</b> component.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@Service
@Slf4j
public class KeyInstallationService {

	@Autowired
	private CryptoProvider cryptoProvider;
	
	@Autowired
	private PKCS11CryptoProvider pkcs11CryptoProvider;

	@Autowired
	private UserRepository userRepository;

	public KeyInstallationService() {
		log.debug("Create new object.");
	}

	/**
	 * Install the given public key for the authentication user.
	 * 
	 * @param publicKey the public key of the user.
	 * @param principal the authentication token
	 * 
	 * @return the result of the installation request.
	 * 
	 * @throws GeneralSecurityException in case the installation fail.
	 */
	public KeyInstallationResult getEncryptionKey(byte[] publicKey, Principal principal) throws GeneralSecurityException {
		log.info("Get encryption key for <{}>", principal.getName());
		if (principal instanceof JwtAuthenticationToken) {
			final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
			jwtAuthenticationToken.getName();
			
			PublicKey parsedPublicKey = SecHSMUtil.parsePublicKey(publicKey);

//			final List<RegisteredUser> allRegisteredUsers = userRepository.findAll();
	//
//			for (RegisteredUser registeredUser : allRegisteredUsers) {
//				log.info("Found the following user <{}> with the public key (hash) <{}>", registeredUser.getEMail(),
//						registeredUser.getPulbicKeyHash());
//			}

			// search public key
			String publicKeyHash = Hex.toHexString(SecHSMUtil.getPublicKeyHash(parsedPublicKey));
			final List<RegisteredUser> registeredUsers = userRepository.findByPulbicKeyHash(publicKeyHash);

			final RegisteredUser registeredUser;
			if (registeredUsers.size() == 0) {
				throw new IllegalStateException("No user with registered public key found (hash) " + publicKeyHash);
			} else if (registeredUsers.size() > 1) {
				throw new IllegalStateException(
						"More than one user with registered public key found (hash) " + publicKeyHash);
			} else {
				registeredUser = registeredUsers.get(0);
			}

			// create and encrypt AES Key
			
			final String deviceInfo = userRepository.findBySubject(registeredUser.getSubject()).get(0).getDeviceInfo();
			final KeyCryptoProvider cryptoProviderImpl = cryptoProvider.createCryptoProvider();
			cryptoProviderImpl.installPublicKey(registeredUser.getSubject(), parsedPublicKey, deviceInfo);

			final KeyInstallationResult wrappingResult = cryptoProviderImpl.getWrappingKey(jwtAuthenticationToken.getToken().getSubject());

			// update registered user

			registeredUser.setEncryptedTestData(Hex.toHexString(wrappingResult.getEncryptedTestData()));
			registeredUser.setEncryptedWrappingKey(Hex.toHexString(wrappingResult.getWrappedSecretKey()));

			userRepository.saveAndFlush(registeredUser);

			return wrappingResult;
			
		} else {
			throw new InsufficientAuthenticationException("Authentication token is unkown: " + principal.getClass().getName());
		}
		

	}

	@PostConstruct
	public void init() {
		// TODO externalize decision which Crypto Provider to load
		// https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config-exposing-yaml-to-spring

		log.info("Initialize all registered users");
		final List<RegisteredUser> allUsers = userRepository.findAll();

		final Map<String, SoftwareCryptoProvider.Triple> registeredUsers = new HashMap<String, SoftwareCryptoProvider.Triple>();

		for (RegisteredUser registeredUser : allUsers) {

			if (registeredUser.getEncryptedWrappingKey() != null) {

				if (registeredUsers.get(registeredUser.getSubject()) == null) {
					log.info("Add registered user <{}>", registeredUser.getEMail());
					final SoftwareCryptoProvider.Triple triple = new SoftwareCryptoProvider.Triple(
							registeredUser.getPublicKey(), Hex.decode(registeredUser.getEncryptedWrappingKey()),
							Hex.decode(registeredUser.getEncryptedTestData()));

					registeredUsers.put(registeredUser.getSubject(), triple);
				} else {
					log.info("Add register user <{}> skipped - already added", registeredUser.getEMail());
				}
			} else {
				log.info("User <{}> has no encrypted key - will not be added", registeredUser.getEMail());
			}
		}

		//keyCryptoProvider = new PKCS11CryptoProvider(); //new SoftwareCryptoProvider(registeredUsers);
	}

	@ExceptionHandler
	void handleGeneralSecurityException(GeneralSecurityException e, HttpServletResponse response) throws IOException {

		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {

		response.sendError(HttpStatus.BAD_REQUEST.value());

	}
}
