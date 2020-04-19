package at.ac.fernfh.remotecrypto.key.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.ac.fernfh.remotecrypto.key.service.KeyInstallationService;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>Key</b> Installation Controller.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@RestController
@RequestMapping("/service")
@Slf4j
public class KeyInstallationController {


	@Autowired
	private KeyInstallationService keyInstallationService;
	
	/**
	 * <b>Key</b> installation request with the passed parameters.
	 *  
	 * @param wrappingRequest the 
	 * @param principal authentication token.
	 * 
	 * @return the key installation response, with the wrapped private key to install.
	 * 
	 * @throws GeneralSecurityException
	 */
	@PostMapping
	public KeyInstallationResult install(@RequestBody KeyInstallationRequest wrappingRequest, Principal principal) throws GeneralSecurityException {
		log.info("Install secret key request");
		
		return keyInstallationService.getEncryptionKey(wrappingRequest.getPublicKey(), principal);
		
//		PublicKey parsedPublicKey = SecHSMUtil.parsePublicKey(publicKey);
//		CryptoService cryptoService = new CryptoService(parsedPublicKey);
//		
//		// search public key
//		String publicKeyHash = Hex.toHexString(publicKey);
//		final List<RegisteredUser> registeredUsers = userRepository.findByPulbicKeyHash(publicKeyHash);
//		
//		
//		final RegisteredUser registeredUser;
//		if (registeredUsers.size() == 0) {
//			throw new IllegalStateException("No user with registered public key found (hash) " + publicKeyHash);
//		} else if (registeredUsers.size() > 0) {
//			throw new IllegalStateException("More than one user with registered public key found (hash) " + publicKeyHash);
//		} else {
//			registeredUser = registeredUsers.get(0);
//		}
//		
//		// create and encrypt AES Key
//
//		final WrappingResult wrappingResult = cryptoService.getEncryptedWrappingKey();
//		
//		// update registered user
//				
//		registeredUser.setEncryptedTestData(Hex.toHexString(wrappingResult.getEncryptedTestData()));
//		registeredUser.setEncryptedWrappingKey(Hex.toHexString(wrappingResult.getWrappedSecretKey()));
//		
//		
//		userRepository.saveAndFlush(registeredUser);
		
//		return wrappingResult;
	}
	
	@ExceptionHandler
	void handleGeneralSecurityException(GeneralSecurityException e, HttpServletResponse response) throws IOException {
		log.error("Error", e);
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		log.error("Error", e);
		response.sendError(HttpStatus.BAD_REQUEST.value());

	}
}
