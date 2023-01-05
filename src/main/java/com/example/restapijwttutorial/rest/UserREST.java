package com.example.restapijwttutorial.rest;


import com.example.restapijwttutorial.document.User;
import com.example.restapijwttutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173/", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", allowCredentials = "true")
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
   public ResponseEntity<?> me(@AuthenticationPrincipal User user, @PathVariable Long id) {
      return ResponseEntity.ok(userRepository.findById(id));
   }


}
