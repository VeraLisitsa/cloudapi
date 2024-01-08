package com.example.cloud_api_v3.security;

import com.example.cloud_api_v3.dto.ErrorDto;
import com.example.cloud_api_v3.util.MappingDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;
import java.io.PrintWriter;

@ExtendWith(MockitoExtension.class)
public class CloudAuthenticationFailureHandlerTest {
    @InjectMocks
    private CloudAuthenticationFailureHandler cloudAuthenticationFailureHandler;

    @Mock
    private MappingDto mappingDto;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException exception;

    @Mock
    private ErrorDto authenticationErrorDto;
    @Mock
    PrintWriter printWriter;

    @Test
    public void onAuthenticationFailureSuccess() throws IOException {

        Mockito.doNothing().when(response).setStatus(Mockito.anyInt());
        Mockito.doNothing().when(response).setContentType(Mockito.anyString());
        Mockito.doNothing().when(response).setCharacterEncoding(Mockito.anyString());
        Mockito.when(mappingDto.errorEntityToErrorDto(Mockito.any())).thenReturn(authenticationErrorDto);
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        cloudAuthenticationFailureHandler.onAuthenticationFailure(request, response, exception);

        Mockito.verify(objectMapper, Mockito.times(1)).writeValue(Mockito.any(PrintWriter.class), Mockito.any(ErrorDto.class));

    }
}
