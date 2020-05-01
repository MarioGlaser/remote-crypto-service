package at.ac.fernfh.remotecrypto.data.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * Configuration properties for the <b>Data</b> Service.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@ConfigurationProperties("remote-crypto-data")
@Data 
public class DataConfiguration {

	private String authentication;
	
	private String storage;
}
