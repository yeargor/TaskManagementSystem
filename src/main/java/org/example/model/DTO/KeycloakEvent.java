package org.example.model.DTO;

import lombok.Data;

@Data
public class KeycloakEvent {
    private String eventType;
    private String payload;
}
