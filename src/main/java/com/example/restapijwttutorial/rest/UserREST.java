package com.example.restapijwttutorial.rest;


import com.example.restapijwttutorial.document.User;
import com.example.restapijwttutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserREST {

   @Autowired
   UserRepository userRepository;

   @GetMapping("/me")
   public ResponseEntity<?> me (@AuthenticationPrincipal User user) {
      return ResponseEntity.ok(user);
   }

   @GetMapping("/{id}")
   @PreAuthorize("#user.id == #id")
   public ResponseEntity<?> me(@AuthenticationPrincipal User user, @PathVariable String id) {
      return ResponseEntity.ok(userRepository.findById(id));
   }


}
