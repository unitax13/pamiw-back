package com.example.restapijwttutorial.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
public class SignupDTO {

   @NotBlank
   @Size(min=4,max=32)
   private String username;
   @NotBlank
   @Size(min=3, max=64)
   private String email;
   @NotBlank
   @Size(min=6, max = 64)
   private String password;

}
