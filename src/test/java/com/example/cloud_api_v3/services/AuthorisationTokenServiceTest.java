package com.example.cloud_api_v3.services;

import com.example.cloud_api_v3.entity.Person;
import com.example.cloud_api_v3.exception.UnauthorizedErrorException;
import com.example.cloud_api_v3.security.PersonDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthorisationTokenServiceTest {

    AuthorisationTokenService authorisationTokenService;
    @Mock
    Authentication authentication;

    @Mock
    PersonDetails personDetails;

    @Mock
    Person person;
    @Mock
    SecurityContext securityContext;

    String authToken = UUID.randomUUID().toString();

    int personId = 1;

    @BeforeEach
    public void beforeEach() {
        authorisationTokenService = new AuthorisationTokenService();
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(personDetails);
        Mockito.when(personDetails.getPerson()).thenReturn(person);
    }

    @AfterEach
    public void afterEach() {
        authorisationTokenService = null;
    }

    @Test
    public void authorisationConfirmationSuccess() {
        Mockito.when(person.getAuthToken()).thenReturn(authToken);
        Mockito.when(personDetails.getPerson().getId()).thenReturn(personId);

        int personIdActual = authorisationTokenService.authorisationConfirmation(authToken, securityContext);
        Assertions.assertEquals(personId, personIdActual);
    }

    @Test
    public void authorisationConfirmationReturnsUnauthorizedError() {
        Mockito.when(person.getAuthToken()).thenReturn(UUID.randomUUID().toString());

        Class<UnauthorizedErrorException> exception = UnauthorizedErrorException.class;
        Assertions.assertThrows(exception, () -> this.authorisationTokenService.authorisationConfirmation(authToken, securityContext));

    }


}
