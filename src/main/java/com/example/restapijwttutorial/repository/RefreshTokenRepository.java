package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.RefreshToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {
   void deleteByOwner_Id(ObjectId id);
   default void deleteByOwnerId(String id) {
      deleteByOwner_Id(new ObjectId(id));
   }
}
