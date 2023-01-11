package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
   Optional<User> findByUsername(String username);
}
