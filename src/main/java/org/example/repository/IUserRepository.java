package org.example.repository;

import org.example.model.User;
import org.springframework.data.repository.CrudRepository;

public interface IUserRepository extends CrudRepository<User, String> {
}
