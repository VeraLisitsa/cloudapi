package com.example.cloud_api_v3.security;

import com.example.cloud_api_v3.entity.Person;
import com.example.cloud_api_v3.util.MappingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class CloudAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final MappingDto mappingDto;
    private final ObjectMapper objectMapper;

    public CloudAuthenticationSuccessHandler(MappingDto mappingDto, ObjectMapper objectMapper) {
        this.mappingDto = mappingDto;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        response.setStatus(HttpServletResponse.SC_OK);
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();
        UUID uuidAuthToken = UUID.randomUUID();
        String authToken = uuidAuthToken.toString();
        person.setAuthToken(authToken);

        final var authenticationTokenDto = mappingDto.personToTokenDto(person);
        objectMapper.writeValue(response.getWriter(), authenticationTokenDto);
    }
}
