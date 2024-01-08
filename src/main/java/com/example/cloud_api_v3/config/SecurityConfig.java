package com.example.cloud_api_v3.config;

import com.example.cloud_api_v3.security.CloudLogoutHandler;
import com.example.cloud_api_v3.security.CloudLogoutSuccessHandler;
import com.example.cloud_api_v3.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final PersonDetailsService personDetailsService;
    private final CloudAuthorisationConfigurer cloudAuthorisationConfigurer;
    private final CloudLogoutHandler cloudLogoutHandler;
    private final CloudLogoutSuccessHandler cloudLogoutSuccessHandler;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, CloudAuthorisationConfigurer cloudAuthorisationConfigurer, CloudLogoutHandler cloudLogoutHandler, CloudLogoutSuccessHandler cloudLogoutSuccessHandler) {
        this.personDetailsService = personDetailsService;
        this.cloudAuthorisationConfigurer = cloudAuthorisationConfigurer;
        this.cloudLogoutHandler = cloudLogoutHandler;
        this.cloudLogoutSuccessHandler = cloudLogoutSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).
                cors(withDefaults()).
                anonymous(AbstractHttpConfigurer::disable).
                formLogin(AbstractHttpConfigurer::disable).
                userDetailsService(personDetailsService).
                authorizeHttpRequests((authorise) -> authorise.anyRequest().authenticated())
                .with(cloudAuthorisationConfigurer, withDefaults())
                .logout(log -> log.logoutUrl("/cloud/logout")
                        .addLogoutHandler(cloudLogoutHandler)
                        .logoutSuccessHandler(cloudLogoutSuccessHandler)
                        .invalidateHttpSession(true));

        return http.build();
    }

}

