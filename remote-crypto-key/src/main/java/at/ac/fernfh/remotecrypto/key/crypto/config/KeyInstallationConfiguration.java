package at.ac.fernfh.remotecrypto.key.crypto.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Configuration properties for the <b>Key</b> Service.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@ConfigurationProperties("remote-crypto-key")
@Data 
public class KeyInstallationConfiguration {
	
	private String authentication;
		
	private String provider;
	
	private String slot;
	
	private String password; 
	
}
