package org.example.service;

import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private final RestTemplate restTemplate = new RestTemplate();

    private String getCurrentAccessToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getTokenValue();
        }
        throw new IllegalStateException("Не удалось получить токен пользователя"); //redo
    }

    public List<Map<String, Object>> getUsers() {
        String url = "http://localhost:8080/admin/realms/Yahor/users";
        String token = getCurrentAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);

        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }
}
