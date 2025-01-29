package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.tasks.User;
import org.example.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;

    public User getUserIfExists(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
