package com.example.cloud_api_v3.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component

public class CloudLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        personDetails.getPerson().setAuthToken(null);
    }
}
