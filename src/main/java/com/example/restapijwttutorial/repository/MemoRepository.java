package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.Memo;

import org.springframework.data.repository.CrudRepository;

public interface MemoRepository extends CrudRepository<Memo, Long> {

}
