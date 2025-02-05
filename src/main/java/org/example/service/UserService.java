package org.example.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.DTO.User.UserDTO;
import org.example.DTO.User.UserDeleteDTO;
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
        UserDTO userDto = mapToUserDTO(payloadNode);
        User user = userRepository.findById(userDto.getId())
                .orElseGet(() -> new User(userDto.getId()));

        updateUserFromDTO(user, userDto);

        userRepository.save(user);
    }

    public void deleteUserByPayload(JsonNode payloadNode) {
        UserDeleteDTO deleteDto = mapToUserDeleteDTO(payloadNode);
        userRepository.deleteById(deleteDto.getId());
    }

    public User getCurrentUser() {
        return getAuthenticatedUserId()
                .flatMap(userRepository::findById)
                .orElseThrow(() -> new IllegalStateException("User not authenticated"));
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

    private UserDTO mapToUserDTO(JsonNode payloadNode) {
        try {
            return objectMapper.treeToValue(payloadNode, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to UserDTO", e);
        }
    }

    private UserDeleteDTO mapToUserDeleteDTO(JsonNode payloadNode) {
        try {
            return objectMapper.treeToValue(payloadNode, UserDeleteDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error mapping JSON to UserDeleteDTO", e);
        }
    }

    private void updateUserFromDTO(User user, UserDTO userDto) {
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
    }

    private Optional<String> getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return Optional.ofNullable(jwtAuth.getTokenAttributes().get("sub").toString());
        }
        return Optional.empty();
    }
}
