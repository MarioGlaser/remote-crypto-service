package at.ac.fernfh.remotecrypto.data.api;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.util.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class SimpleDataController {

	@PostMapping
	public void data(@RequestBody SimpleData data, Principal principal) {
		log.info("Upload file");
		
		if (principal instanceof JwtAuthenticationToken) {
			final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
			final String subject = jwtAuthenticationToken.getToken().getSubject();
			
			log.info("Upload file for user <{}> with name <{}>", subject, data.getFileName());
			
			
			final File directory = new File("./target/"+subject+"/");
			if (!directory.exists() && !directory.mkdirs()) {
				throw new IllegalStateException("Could not create directories for path <{}>");
			}
			FileUtil.writeAsString(new File(directory, data.getFileName()), data.getEncryptedData());
		} else {
			throw new InsufficientAuthenticationException("Authentication token is unkown: " + principal.getClass().getName());
		}
	}
	
	@GetMapping
	public List<SimpleData> data(Principal principal) {
		log.info("List all files");

		if (principal instanceof JwtAuthenticationToken) {
			final JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) principal;
			final String subject = jwtAuthenticationToken.getToken().getSubject();
			
			log.info("List files for user <{}>", subject);
			
			
			final File directory = new File("./target/"+subject+"/");

			final List<SimpleData> dataList = new ArrayList<>();
			
			final File[] files = directory.listFiles();
			for (File file : files) {
				String encryptedData;
				try {
					encryptedData = FileUtil.readAsString(file);
				} catch (IOException e) {
					throw new IllegalStateException("Could not load data from file <{}>", e);
				}
				final SimpleData data = new SimpleData();
				data.setEncryptedData(encryptedData);
				data.setFileName(file.getName());
				dataList.add(data);
			}
			
			return dataList;
			
		} else {
			throw new InsufficientAuthenticationException("Authentication token is unkown: " + principal.getClass().getName());
		}
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
