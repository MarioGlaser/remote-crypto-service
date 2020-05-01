package at.ac.fernfh.remotecrypto.key;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Sprint Boot startup class for the <code>Key</code> service.
 * 
 * @author Mario Glaser
 * @since 1.0
 */
@EnableJpaRepositories("at.ac.fernfh.remotecrypto.model")
@EntityScan("at.ac.fernfh.remotecrypto.model")
@ConfigurationPropertiesScan
@EnableSwagger2
@SpringBootApplication
public class RemoteCryptoKeyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RemoteCryptoKeyApplication.class, args);
	}

}
