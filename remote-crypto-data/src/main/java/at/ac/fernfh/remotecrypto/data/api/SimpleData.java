package at.ac.fernfh.remotecrypto.data.api;

import lombok.Data;

/**
 * <code>Data</code> request/response object.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@Data
public class SimpleData {

	/** File name. */
	private String fileName;
	
	/** Encrypted data. */
	private String encryptedData;
}
