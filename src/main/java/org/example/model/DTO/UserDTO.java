package org.example.model.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
}