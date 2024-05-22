package com.remotelab.externalApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    // creates a filter each incoming request has to pass through
    // the authorization header is extracted and validated
    // https://auth0.com/docs/quickstart/backend/java-spring-security5/interactive
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequest) ->
                authorizeHttpRequest.anyRequest().authenticated()).oauth2ResourceServer(oauth2 -> oauth2
                .jwt(withDefaults())
        );
        return http.build();
    }
}
