package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.tasks.User;
import org.example.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final KeycloakService keycloakService;

    public User getUserIfExists(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public List<User> syncUsersFromKeycloak() {
        List<Map<String, Object>> keycloakUsers = keycloakService.getUsers();
        List<User> savedUsers = new ArrayList<>();

        for (Map<String, Object> keycloakUser : keycloakUsers) {
            String id = (String) keycloakUser.get("id");

            if (!userRepository.existsById(id)) {
                User user = new User();
                user.setId(id);
                savedUsers.add(userRepository.save(user));
            }
        }
        return savedUsers;
    }
}


//TODO: синхронизировать наличие users между keycloak и app, проблема наличия айди
//при создании тасков (- Users может не быть и их нужно подтягивать из рилма
//в целом проверить цепочку оборота