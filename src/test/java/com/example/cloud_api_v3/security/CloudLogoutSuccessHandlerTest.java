package com.example.cloud_api_v3.security;

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
public class CloudLogoutSuccessHandlerTest {
    @InjectMocks
    private CloudLogoutSuccessHandler cloudLogoutSuccessHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Test
    public void onLogoutSuccessSuccess(){
        cloudLogoutSuccessHandler.onLogoutSuccess(request,response,authentication);
        Mockito.verify(response, Mockito.times(1)).setStatus(Mockito.anyInt());
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
