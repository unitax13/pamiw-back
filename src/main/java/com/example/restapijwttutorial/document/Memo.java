package com.example.restapijwttutorial.document;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.Id;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Memo {

   @Id
   @GeneratedValue(strategy= GenerationType.AUTO)
   private Long id;

   @ManyToOne
   @JoinColumn(name="user_id")
   private User user;

   private String memoContent;

   private String sanitizedHtml;

   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   @OneToMany(fetch = FetchType.EAGER, mappedBy = "memo",cascade = CascadeType.REMOVE)
   private List<SharingLink> sharingLinks;

   public Memo() {

   }

   public Memo(String content) {
      this.memoContent = content;
   }

   //@JsonIgnore
   public List<SharingLink> getSharingLinks() {
      return sharingLinks;
   }

   public void setSharingLinks(List<SharingLink> sharingLinks) {
      this.sharingLinks = sharingLinks;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
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
