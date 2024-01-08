package com.example.cloud_api_v3.services;

import com.example.cloud_api_v3.exception.UnauthorizedErrorException;
import com.example.cloud_api_v3.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationTokenService {
    private final String BEARER = "Bearer ";

    public int authorisationConfirmation(String authTokenRaw, SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String authToken = authTokenRaw.replaceFirst(BEARER, "");
        if (!authToken.equals(personDetails.getPerson().getAuthToken())) {
            throw new UnauthorizedErrorException("Unauthorized error");
        }
        return personDetails.getPerson().getId();
    }
}
