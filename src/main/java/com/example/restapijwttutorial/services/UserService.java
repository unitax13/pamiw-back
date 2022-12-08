package com.example.restapijwttutorial.services;

import com.example.restapijwttutorial.document.User;
import com.example.restapijwttutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
   @Autowired
   UserRepository userRepository;
   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
   }

   public User findById(String id) {
      //System.out.println("Searching for user with id: " + id);
      return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user id not found"));
   }

}
