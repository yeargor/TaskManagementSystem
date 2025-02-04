package org.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/keycloak-events/user-updated")  // Исправленный путь, чтобы совпадал с SPI
@Slf4j
public class KeycloakEventController {

    @Value("${keycloak.webhook.secret}")  // Читаем секретный ключ из настроек
    private String expectedSecret;

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public KeycloakEventController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> handleKeycloakUserEvent(
            @RequestHeader("X-Keycloak-Secret") String receivedSecret,
            @RequestBody Map<String, Object> eventData) {

        // Проверяем секретный ключ
        if (!expectedSecret.equals(receivedSecret)) {
            log.warn("Unauthorized request: invalid secret");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden: Invalid secret");
        }

        try {
            log.info("Received Keycloak event: {}", eventData);

            // Извлекаем eventType
            String eventType = (String) eventData.get("eventType");

            JsonNode payloadNode;
            if (eventData.containsKey("payload") && eventData.get("payload") != null) {
                payloadNode = objectMapper.valueToTree(eventData.get("payload"));
            } else {
                payloadNode = objectMapper.valueToTree(eventData);
            }
            log.info("Extracted eventType: {}. Payload: {}", eventType, payloadNode.toString());

            // Дополнительная проверка: убедимся, что payloadNode является объектом и содержит "id"
            if (!payloadNode.isObject() || !payloadNode.has("id")) {
                log.error("Payload is null or missing 'id'. PayloadNode: {}", payloadNode.toString());
                return ResponseEntity.badRequest().body("Invalid payload");
            }

            // Выбираем действие в зависимости от eventType
            switch (eventType != null ? eventType : "CREATE") {
                case "CREATE":
                case "UPDATE":
                    userService.createOrUpdateUserFromPayload(payloadNode);
                    break;
                case "DELETE":
                    userService.deleteUserByPayload(payloadNode);
                    break;
                default:
                    log.warn("Unknown eventType: {}", eventType);
            }
            return ResponseEntity.ok("User processed successfully");
        } catch (Exception e) {
            log.error("Error handling Keycloak event", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing user payload: " + e.getMessage());
        }
    }
}
