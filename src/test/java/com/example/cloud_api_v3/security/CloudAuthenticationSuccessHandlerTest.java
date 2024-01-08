package com.example.cloud_api_v3.security;

import com.example.cloud_api_v3.dto.TokenDto;
import com.example.cloud_api_v3.entity.Person;
import com.example.cloud_api_v3.util.MappingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;

@ExtendWith(MockitoExtension.class)
public class CloudAuthenticationSuccessHandlerTest {
    @InjectMocks
    private CloudAuthenticationSuccessHandler cloudAuthenticationSuccessHandler;
    @Mock
    private MappingDto mappingDto;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private HttpSession session;
    @Mock
    private PersonDetails personDetails;
    @Mock
    private Person person;
    @Mock
    private TokenDto authenticationTokenDto;
    @Mock
    private PrintWriter printWriter;

    @Test
    public void onAuthenticationSuccessSuccess() throws IOException {
        Mockito.when(request.getSession(true)).thenReturn(session);
        Mockito.doNothing().when(session).setAttribute(Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(response).setStatus(Mockito.anyInt());
        Mockito.when(authentication.getPrincipal()).thenReturn(personDetails);
        Mockito.when(personDetails.getPerson()).thenReturn(person);
        Mockito.doNothing().when(person).setAuthToken(Mockito.anyString());
        Mockito.when(mappingDto.personToTokenDto(person)).thenReturn(authenticationTokenDto);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        cloudAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        Mockito.verify(objectMapper, Mockito.times(1)).writeValue(Mockito.any(PrintWriter.class), Mockito.any(Object.class));

    }
}
