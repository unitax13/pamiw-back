package com.example.restapijwttutorial.document;

import lombok.Data;
import javax.persistence.Id;


import javax.persistence.*;

@Entity
@Data
public class RefreshToken {

   @Id
   @GeneratedValue(strategy= GenerationType.AUTO)
   private Long id;

   @ManyToOne
   @JoinColumn(name="user_id")
   private User owner;
}
