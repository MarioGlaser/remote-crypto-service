package at.ac.fernfh.remotecrypto.registration.api;

import at.ac.fernfh.remotecrypto.model.RegisteredUser;
import at.ac.fernfh.remotecrypto.model.repository.UserRepository;
import at.ac.fernfh.remotecrypto.registration.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public void register(@RequestBody Registration registration, Principal principal) throws GeneralSecurityException {
		log.info("Register a new Public Key for <{}>", principal.getName());

		
		if (principal instanceof JwtAuthenticationToken) {
			final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
			jwtAuthenticationToken.getName();
			
			registrationService.registerAppForUser(registration, jwtAuthenticationToken);
			
		} else {
			throw new InsufficientAuthenticationException("Authentication token is unkown: " + principal.getClass().getName());
		}
		
		
		log.info("Register object {}", registration);
	}

	@GetMapping
	public List<RegisteredUser> register() {
		log.info("List all registered users");

		return userRepository.findAll();
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
