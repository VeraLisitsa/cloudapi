package com.example.cloud_api_v3.config;


import com.example.cloud_api_v3.security.CloudAuthenticationFailureHandler;
import com.example.cloud_api_v3.security.CloudAuthenticationSuccessHandler;
import com.example.cloud_api_v3.security.CloudAuthorisationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class CloudAuthorisationConfigurer extends AbstractHttpConfigurer<CloudAuthorisationConfigurer, HttpSecurity> {

    private final CloudAuthenticationSuccessHandler cloudAuthenticationSuccessHandler;
    private final CloudAuthenticationFailureHandler cloudAuthenticationFailureHandler;


    @Autowired
    public CloudAuthorisationConfigurer(CloudAuthenticationSuccessHandler cloudAuthenticationSuccessHandler, CloudAuthenticationFailureHandler cloudAuthenticationFailureHandler) {
        this.cloudAuthenticationSuccessHandler = cloudAuthenticationSuccessHandler;
        this.cloudAuthenticationFailureHandler = cloudAuthenticationFailureHandler;
    }


    @Override
    public void init(HttpSecurity builder) throws Exception {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity builder) {
        final var authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        final var cloudAuthorisationFilter = new CloudAuthorisationFilter(authenticationManager);
        cloudAuthorisationFilter.setAuthenticationFailureHandler(cloudAuthenticationFailureHandler);
        cloudAuthorisationFilter.setAuthenticationSuccessHandler(cloudAuthenticationSuccessHandler);

        builder.addFilterAt(cloudAuthorisationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}