package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.Memo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemoRepository extends MongoRepository<Memo, String> {

}
