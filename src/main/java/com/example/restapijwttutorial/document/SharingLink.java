package com.example.restapijwttutorial.document;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class SharingLink {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @JsonIgnore
   @ManyToOne//(cascade = CascadeType.DETACH)
   @JoinColumn(name="memo_id")
   private Memo memo;

   private String link;
   private boolean canEdit;

   private LocalDateTime createdAt;

}
