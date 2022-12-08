package com.example.restapijwttutorial.document;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import java.time.LocalDateTime;

@Document
@Data
public class Memo {

   @Id
   private String id;

   //TODO: Roles system, links system

   @DocumentReference
   private User user;

   private String memoContent;

   private String sanitizedHtml;

   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   public Memo() {

   }

   public Memo(String content) {
      this.memoContent = content;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   @JsonIgnore
   public User getUser() {
      return user;
   }

   public void setUser(User user) {
      this.user = user;
   }

   public String getMemoContent() {
      return memoContent;
   }

   public void setMemoContent(String memoContent) {
      this.memoContent = memoContent;
   }

   public String getSanitizedHtml() {
      return sanitizedHtml;
   }

   public void setSanitizedHtml(String sanitizedHtml) {
      this.sanitizedHtml = sanitizedHtml;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   public LocalDateTime getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
   }
}
