package com.example.cloud_api_v3.security;

import com.example.cloud_api_v3.entity.ErrorEntity;
import com.example.cloud_api_v3.util.MappingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CloudAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final String ERROR_MESSAGE = "Bad credentials";
    private final MappingDto mappingDto;
    private final ObjectMapper objectMapper;

    @Autowired
    public CloudAuthenticationFailureHandler(MappingDto mappingDto, ObjectMapper objectMapper) {
        this.mappingDto = mappingDto;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        final var authenticationError = new ErrorEntity(ERROR_MESSAGE);
        final var authenticationErrorDto = mappingDto.errorEntityToErrorDto(authenticationError);
        objectMapper.writeValue(response.getWriter(), authenticationErrorDto);
    }
}
