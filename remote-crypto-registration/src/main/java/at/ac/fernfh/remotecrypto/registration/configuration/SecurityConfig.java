package at.ac.fernfh.remotecrypto.registration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * OpenID bzw. OAuth 2.0 Konfiguration der <b>Registrierungs-Komponente</b>.
 *  
 * @author Mario Glaser
 * @since 1.0
 */
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .oauth2Login().and()
                    .oauth2ResourceServer().jwt().jwkSetUri("https://dev-854566.okta.com/oauth2/v1/keys");
            // @formatter:on
        }
    }
    
    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistrations
                .fromOidcIssuerLocation("https://dev-854566.okta.com")
                .clientId("0oa23ougmuaD6UkXg357")
                .build();
        
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
