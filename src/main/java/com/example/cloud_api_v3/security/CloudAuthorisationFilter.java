package com.example.cloud_api_v3.security;

import com.example.cloud_api_v3.dto.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;


public class CloudAuthorisationFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    private AuthenticationManager authenticationManager;

    public CloudAuthorisationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/cloud/login", "POST"), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username;
        String password;
        try {

            LoginDto login = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            username = login.getLogin();
            password = login.getPassword();

        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        final var authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        authenticationManager = this.getAuthenticationManager();
        final var authResult = authenticationManager.authenticate(authRequest);

        return authResult;
    }

}
