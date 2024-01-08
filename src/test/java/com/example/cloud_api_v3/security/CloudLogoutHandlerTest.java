package com.example.cloud_api_v3.security;

import com.example.cloud_api_v3.entity.Person;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
public class CloudLogoutHandlerTest {
    @InjectMocks
    private CloudLogoutHandler cloudLogoutHandler;
    @Mock
    private PersonDetails personDetails;
    @Mock
    private Authentication authentication;
    @Mock
    private Person person;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;


    @Test
    public void logoutSuccess() {

        Mockito.when(authentication.getPrincipal()).thenReturn(personDetails);
        Mockito.when(personDetails.getPerson()).thenReturn(person);
        cloudLogoutHandler.logout(request, response, authentication);

        Mockito.verify(person, Mockito.times(1)).setAuthToken(Mockito.any());
        Mockito.verify(person).setAuthToken(null);
    }
}
