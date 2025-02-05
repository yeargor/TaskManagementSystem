package org.example.model.DTO;

import lombok.Data;

@Data
public class KeycloakEventDTO {
    private String eventType;
    private String payload;
}
