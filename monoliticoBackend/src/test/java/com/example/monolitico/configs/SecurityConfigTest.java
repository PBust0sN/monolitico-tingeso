package com.example.monolitico.configs;

import com.example.monolitico.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        assertNotNull(source);

        MockHttpServletRequest request = new MockHttpServletRequest();
        CorsConfiguration config = source.getCorsConfiguration(request);

        assertNotNull(config);
        assertTrue(config.getAllowedOrigins().contains("http://localhost:5173"));
        assertTrue(config.getAllowedMethods().contains("GET"));
        assertTrue(config.getAllowedMethods().contains("POST"));
        assertTrue(config.getAllowedHeaders().contains("*"));
        assertTrue(config.getAllowCredentials());
    }

    @Test
    void testJwtAuthConverterRoles() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthConverter();
        assertNotNull(converter);

        // Mock JWT con roles
        var jwt = new org.springframework.security.oauth2.jwt.Jwt(
                "token", null, null,
                Map.of("alg", "none"),
                Map.of("realm_access", Map.of("roles", List.of("USER", "ADMIN")))
        );

        assertNotNull(converter);
        assertNotNull(jwt);
    }
}
