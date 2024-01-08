package com.example.cloud_api_v3.security;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;

@ExtendWith(MockitoExtension.class)
public class CloudAuthorisationFilterTest {
    @InjectMocks
    private CloudAuthorisationFilter cloudAuthorisationFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @Mock
    private HttpServletResponse response;


    @Test
    public void attemptAuthenticationSuccess() {

        String msg = "{\"login\":\"userTest\", \"password\":\"passTest\"}";
        mockHttpServletRequest.setContent(msg.getBytes());

        cloudAuthorisationFilter.attemptAuthentication(mockHttpServletRequest, response);

        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(Mockito.any());

    }

    @Test
    public void attemptAuthenticationReturnsAuthenticationServiceException() {
        String msg = "";
        mockHttpServletRequest.setContent(msg.getBytes());

        Class<AuthenticationServiceException> exception = AuthenticationServiceException.class;
        Assertions.assertThrows(exception, ()-> cloudAuthorisationFilter.attemptAuthentication(mockHttpServletRequest, response));
    }
}
