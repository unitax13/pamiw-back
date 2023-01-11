package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.RefreshToken;

import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
   void deleteByOwner_Id(Long id);
   default void deleteByOwnerId(Long id) {
      deleteByOwner_Id(id);
   }
}
