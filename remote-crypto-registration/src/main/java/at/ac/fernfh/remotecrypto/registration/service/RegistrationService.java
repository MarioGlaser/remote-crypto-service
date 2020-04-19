package at.ac.fernfh.remotecrypto.registration.service;

import at.ac.fernfh.remotecrypto.model.RegisteredUser;
import at.ac.fernfh.remotecrypto.model.repository.UserRepository;
import at.ac.fernfh.remotecrypto.registration.api.Registration;
import at.ac.fernfh.remotecrypto.util.SecHSMUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Service implementation of the <b>Registration</b> component.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@Service
@Slf4j
public class RegistrationService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Register a new user with the passed parameters.
	 * 
	 * @param registration the registration parameter of the user.
	 * @param jwtAuthenticationToken the authentication token.
	 * 
	 * @throws GeneralSecurityException in case the registration fail.
	 */
	@Transactional
	public void registerAppForUser(Registration registration, JwtAuthenticationToken jwtAuthenticationToken)
			throws GeneralSecurityException {
		log.info("Register user {}", registration);

		PublicKey publicKey = SecHSMUtil.parsePublicKey(registration.getPublicKey());

		log.info("Register public key for user {}",
				jwtAuthenticationToken.getToken().getClaimAsString("preferred_username"));

		RegisteredUser user = new RegisteredUser();

		jwtAuthenticationToken.getName(); // 00uxzpl26kIFV1EEo356
		jwtAuthenticationToken.getAuthorities(); // empty
		jwtAuthenticationToken.getDetails(); // remote Adress
		jwtAuthenticationToken.getTokenAttributes();
		jwtAuthenticationToken.getToken().getAudience(); // 0oa23ougmuaD6UkXg357
		jwtAuthenticationToken.getToken().getId(); // ID.FL6HsTceqH3fMjvHBQ8kdpaIso3wGN56ydFxmvQ1jwE
		jwtAuthenticationToken.getToken().getIssuer(); // https://dev-854566.okta.com
		jwtAuthenticationToken.getToken().getSubject(); // 00uxzpl26kIFV1EEo356
		jwtAuthenticationToken.getToken().getTokenValue(); // eyJraWQiOiJ2TFlPMTdVRHJaYzNkZC1nSk1jaEk4Tmttc0JVYlNJZG5qOW1pdkNGcWg4IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIwMHV4enBsMjZrSUZWMUVFbzM1NiIsIm5hbWUiOiJNYXJpbyBHbGFzZXIiLCJ2ZXIiOjEsImlzcyI6Imh0dHBzOi8vZGV2LTg1NDU2Ni5va3RhLmNvbSIsImF1ZCI6IjBvYTIzb3VnbXVhRDZVa1hnMzU3IiwiaWF0IjoxNTc1NzYxNDIzLCJleHAiOjE1NzU3NjUwMjMsImp0aSI6IklELkZMNkhzVGNlcUgzZk1qdkhCUThrZHBhSXNvM3dHTjU2eWRGeG12UTFqd0UiLCJhbXIiOlsicHdkIl0sImlkcCI6IjAwb3h6b3Q2M3dFa3ZGWHRMMzU2Iiwibm9uY2UiOiJUV0FuNjBKXzRJQ25aZHkwR1BJbk9nIiwicHJlZmVycmVkX3VzZXJuYW1lIjoibWFyaW8uZ2xhc2VyQG1haWwuZmVybmZoLmFjLmF0IiwiYXV0aF90aW1lIjoxNTc1NzYxNDIxLCJhdF9oYXNoIjoiS1UyNThxNjJCZ01QbnJKeGUwQzE1USJ9.TgzXkCEgbHzuUN2JNVsq71Nc7-e1M8OGaQt_8xc3omAkbZzE7hBp35eGHNJi7bVoOOrmYlH9rAR8Tl3JrwMUAomHSXceU8KMh4SRVBjvU5cCLY-pODWwe6DkVtnm6V5TZSaK0SEr7RDJURpKBLkpii_AOU9yUCjPU8TcYFsPPJvBsi-bQtz3qhL9RUi7GzcEyRdUCbplUFkrNABZtSisBo83R8dQRykLR-5larq-KfAKgmsiUBX4zApov2If279Q6psNmO-N8leqJVZONuhXpafbvnrbMVRZkaxU_YbuDXM831M9qzWqFGtbGssNYhrMo0NBR79jZw2Y92HtoEjkMw
		jwtAuthenticationToken.getToken().getHeaders(); // header kid und algorithm
		jwtAuthenticationToken.getToken().getClaimAsString(org.springframework.security.oauth2.jwt.JwtClaimNames.SUB); // wie
																														// get
																														// name
		jwtAuthenticationToken.getToken().getClaimAsString("preferred_username"); // mario.glaser@mail.fernfh.ac.at
		jwtAuthenticationToken.getToken().getClaimAsInstant("auth_time"); // 2019-12-07T23:30:21Z

		// TODO Parse idToken
		user.setIdToken(jwtAuthenticationToken.getToken().getTokenValue());
		user.setEMail(jwtAuthenticationToken.getToken().getClaimAsString("preferred_username"));
		user.setAuthTime(jwtAuthenticationToken.getToken().getClaimAsInstant("auth_time"));
		user.setExpTime(jwtAuthenticationToken.getToken().getClaimAsInstant("exp"));
		user.setSubject(jwtAuthenticationToken.getToken().getSubject());

		user.setPublicKey(SecHSMUtil.parsePublicKey(registration.getPublicKey()));
		user.setPulbicKeyValue(Base64.getEncoder().encodeToString(registration.getPublicKey()));
		user.setPulbicKeyHash(Hex.toHexString(SecHSMUtil.getPublicKeyHash(publicKey)));
		user.setDeviceInfo(registration.getDeviceInfo());

		userRepository.save(user);

		log.info("Registered public key for user {} with pk hash {}", user.getEMail(), user.getPulbicKeyHash());
	}

}
