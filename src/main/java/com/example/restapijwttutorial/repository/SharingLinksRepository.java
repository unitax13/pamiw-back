package com.example.restapijwttutorial.repository;

import com.example.restapijwttutorial.document.SharingLink;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SharingLinksRepository extends CrudRepository<SharingLink, Long> {
   Optional<SharingLink> findByLink(String link);
}
