package org.example.controller;

import org.example.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final KeycloakService keycloakService;

    @GetMapping("/")
    @PreAuthorize("hasRole('CLIENT')")
    public String home(){
        return "home";
    }

    @GetMapping("/accessDenied")
    public String accessDenied(){ return  "accessDenied"; }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getUsers() {
        return ResponseEntity.ok(keycloakService.getUsers());
    }
}
