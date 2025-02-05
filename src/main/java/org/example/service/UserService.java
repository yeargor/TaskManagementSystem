package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.DTO.UserDTO;
import org.example.model.DTO.UserDeleteDTO;
import org.example.model.User;
import org.example.repository.IUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;
    private final ObjectMapper objectMapper;

    public void createOrUpdateUserFromPayload(JsonNode payloadNode) {
        try {
            UserDTO userDto = objectMapper.treeToValue(payloadNode, UserDTO.class);
            User user = userRepository.findById(userDto.getId())
                    .orElseGet(() -> new User(userDto.getId()));

            user.setUsername(userDto.getUsername());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());

            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error processing user payload", e);
        }
    }

    public void deleteUserByPayload(JsonNode payloadNode) {
        try {
            UserDeleteDTO deleteDto = objectMapper.treeToValue(payloadNode, UserDeleteDTO.class);
            userRepository.deleteById(deleteDto.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error processing delete payload", e);
        }
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String userId = jwtAuth.getTokenAttributes().get("sub").toString();
            return userRepository.findById(userId).orElse(null);
        }
        throw new IllegalStateException("Ошибка аутентификации");
    }

    public boolean currentUserHasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }
}
