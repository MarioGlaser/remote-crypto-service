package at.ac.fernfh.remotecrypto.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Sprint Boot startup class for the <code>Data</code> service.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class RemoteCryptoDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemoteCryptoDataApplication.class, args);
	}

}
