package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
   Optional<User> findByUsername(String username);
}
