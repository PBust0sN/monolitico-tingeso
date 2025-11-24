package com.example.monolitico.Services;

import com.example.monolitico.Service.KeycloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class KeycloakServiceTest {

    @InjectMocks
    private KeycloakService keycloakService;

    @Mock
    private RestTemplate restTemplate;

    private static final String MOCK_TOKEN = "mocked-admin-token";

    @BeforeEach
    void setUp() {
        // inyectar RestTemplate mock dentro de KeycloakService si fuera necesario
    }

    @Test
    void testGetAdminToken() {
        // Mockear la respuesta de RestTemplate
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(
                Map.of("access_token", MOCK_TOKEN), HttpStatus.OK
        );

        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(responseEntity);

        // Llamada real al método
        String token = keycloakService.getAdminToken();

        assertNotNull(token);
    }



}
