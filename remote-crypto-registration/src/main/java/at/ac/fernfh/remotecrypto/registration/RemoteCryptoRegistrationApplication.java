package at.ac.fernfh.remotecrypto.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableJpaRepositories("at.ac.fernfh.remotecrypto.model")
@EntityScan("at.ac.fernfh.remotecrypto.model")
@EnableSwagger2
@SpringBootApplication
public class RemoteCryptoRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteCryptoRegistrationApplication.class, args);
    }

}
