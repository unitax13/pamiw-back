package com.example.restapijwttutorial.rest;


import com.example.restapijwttutorial.document.Memo;
import com.example.restapijwttutorial.document.SharingLink;
import com.example.restapijwttutorial.document.User;
import com.example.restapijwttutorial.repository.MemoRepository;
import com.example.restapijwttutorial.repository.SharingLinksRepository;
import com.example.restapijwttutorial.repository.UserRepository;
import com.example.restapijwttutorial.utils.SecureUtils;
import com.example.restapijwttutorial.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173/", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(path = "/api/memos")
public class MemoREST {
   @Autowired
   private MemoRepository memoRepository;
   @Autowired
   private UserRepository userRepository;
   @Autowired
   private SharingLinksRepository sharingLinksRepository;


   @GetMapping (path= "/shared/{id}")
   public ResponseEntity<?> getMemoSharedByLink (@PathVariable("id") String id) {

      if (sharingLinksRepository.findByLink(id).isPresent()) {
         System.out.println("Can edit? " + sharingLinksRepository.findByLink(id).get().isCanEdit());
         Memo memo = sharingLinksRepository.findByLink(id).get().getMemo();
         return new ResponseEntity<Object>(memo, HttpStatus.OK);
      } else {
         return new ResponseEntity<Object>("Link " + id + " is invalid.", HttpStatus.NOT_FOUND);
      }
   }

   @PutMapping (path= "/shared/{id}")
   public ResponseEntity<?> editMemoSharedByLink (@PathVariable("id") String id,@RequestParam String content) {
      if (sharingLinksRepository.findByLink(id).isPresent()) {
         if (sharingLinksRepository.findByLink(id).get().isCanEdit()) {
            ///this can edit
            Memo memo = sharingLinksRepository.findByLink(id).get().getMemo();
            memo.setMemoContent(content);
            memo.setSanitizedHtml(Utils.parseMarkdown(content));
            memo.setUpdatedAt(LocalDateTime.now());
            memoRepository.save(memo);
            return new ResponseEntity<>(memo, HttpStatus.OK);


         } else {
            return new ResponseEntity<Object>("Cannot edit this resource.", HttpStatus.OK);
         }
      }
      return new ResponseEntity<Object>("Link " + id + " is invalid.", HttpStatus.NOT_FOUND);
   }


   @GetMapping
   public ResponseEntity<?> getAll(@AuthenticationPrincipal User user) {

         return new ResponseEntity<Object>(user.getMemos(), HttpStatus.OK);

//      return new ResponseEntity<Object>("Not found", HttpStatus.OK);
   }


   @GetMapping (path = "/{id}")
   ResponseEntity<?> getOne(@AuthenticationPrincipal User user, @PathVariable("id") Long memoId) {
      //because this allows us to get a memo of any user, we need to check, whether the user on the memo is the user authenticated
      // we do that by using this equals on the users' id
      if (memoRepository.existsById(memoId)) {
         if (memoRepository.findById(memoId).get().getUser().getId().equals(user.getId())) {
            return new ResponseEntity<Object>(memoRepository.findById(memoId), HttpStatus.OK);
         } else {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
         }
      }
      else return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

   }

   // @desc Add memo
// @route POST /api/memos
// @access Private
   @PostMapping
   public ResponseEntity<?> addMemo(@AuthenticationPrincipal User user, @RequestParam String content) {

         Memo newMemo = new Memo(content);
         newMemo.setSanitizedHtml(Utils.parseMarkdown(content));
         newMemo.setUser(user);
         newMemo.setCreatedAt(LocalDateTime.now());
         newMemo.setUpdatedAt(LocalDateTime.now());

         memoRepository.save(newMemo);

         return new ResponseEntity<Object>(newMemo,HttpStatus.OK);

   }

   // @desc Edit memo
// @route PUT /api/memos
// @access Private
   @PutMapping
   public ResponseEntity<Object> updateMemo(@AuthenticationPrincipal User user, @RequestParam Long memoId, @RequestParam String content) {
      if (memoRepository.existsById(memoId)) {
//because this allows us to get a memo of any user, we need to check, whether the user on the memo is the user authenticated

// here we go through users' memos and check if one of them matches id
         List<Memo> newMemos = user.getMemos();
         for (int i=0; i< newMemos.size(); i++) {
            Memo memo = newMemos.get(i);
            if (memo.getId() != null && memo.getId().equals(memoId)) {
               memo.setMemoContent(content);
               memo.setSanitizedHtml(Utils.parseMarkdown(content));
               memo.setUpdatedAt(LocalDateTime.now());
               memoRepository.save(memo);

               return new ResponseEntity<Object>(memo,HttpStatus.OK);
            }
         }

         return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);


      } else {
         return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
      }
   }

   // @desc Delete memo
// @route DELETE /api/memos
// @access Private
   @DeleteMapping
   public ResponseEntity<Object> deleteMemo(@AuthenticationPrincipal User user, @RequestParam Long memoId) {
      if (memoRepository.existsById(memoId)) {
         List<Memo> newMemos = user.getMemos();

         for (int i=0; i< newMemos.size(); i++) {
            Memo memo = newMemos.get(i);
            if (memo.getId() != null && memo.getId().equals(memoId)) {
//               newMemos.remove(i);
//               user.setMemos(newMemos);
//               userRepository.save(user);
               memoRepository.deleteById(memoId);
               return new ResponseEntity<Object>(memo, HttpStatus.OK);
            }
         }

      }
         return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);

   }

   @PostMapping(path = "/share")
   public ResponseEntity<?> generateALinkToTheMemo(@AuthenticationPrincipal User user, @RequestParam Long memoId, @RequestParam(required = false) String canEdit) throws NoSuchAlgorithmException {
      boolean canEditBool = false;
      if (canEdit != null && canEdit.equalsIgnoreCase("true") ) {
         canEditBool = true;
      }

      if (memoRepository.existsById(memoId)) {
         if (memoRepository.findById(memoId).get().getUser().getId().equals(user.getId())) {

               SharingLink newSharingLink = new SharingLink();
               newSharingLink.setMemo(memoRepository.findById(memoId).get());
               newSharingLink.setCreatedAt(LocalDateTime.now());
               newSharingLink.setCanEdit(canEditBool);

               String uselessJunk = SecureUtils.generateRandomAlphanumericString(16);
               System.out.println("Useless junk: " + uselessJunk);

               newSharingLink.setLink(uselessJunk);

//               List<SharingLink> newSharingLinksList = memo.getSharingLinks();
//               newSharingLinksList.add(newSharingLink);
//               memo.setSharingLinks(newSharingLinksList);

            newSharingLink = sharingLinksRepository.save(newSharingLink);
//               memoRepository.save(memo);

               return new ResponseEntity<Object>(newSharingLink.getLink(), HttpStatus.OK);
            }
         }
      return new ResponseEntity<>("Memo not found",HttpStatus.BAD_REQUEST);
   }





   // @desc Get memos
   // @route GET /api/memos
   // @access Private
//   @GetMapping
//   public ResponseEntity<Object> getMemosByUserId (@RequestParam String userId, @RequestHeader (required = false) String requestHeader) {
//
//      if (userRepository.findById(userId).isPresent()) {
//         return new ResponseEntity<>(userRepository.findById(userId).get().getMemoList(), HttpStatus.OK);
//
//      }
//
//      return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.NOT_FOUND);
//
//   }
//
//// @desc Add new memo
//// @route POST /api/memos
//// @access Private
//   @PostMapping
//   public ResponseEntity<Object> setMemo (@RequestParam Integer userId, @RequestParam String content) {
//      if (userRepository.findById(userId).isPresent()) {
//         User user = userRepository.findById(userId).get();
//         List<Memo> list = user.getMemoList();
//         Memo newMemo = new Memo(content);
//         newMemo.setSanitizedHtml(Utils.parseMarkdown(content));
//         newMemo.setUser(user);
//         newMemo.setCreatedAt(LocalDateTime.now());
//         newMemo.setUpdatedAt(LocalDateTime.now());
//
//
//         list.add(newMemo);
//         user.setMemoList(list);
//         userRepository.save(user);
//         memoRepository.save(newMemo);
//
//         return new ResponseEntity<Object>(newMemo,HttpStatus.OK);
//      } else
//         return new ResponseEntity<Object>("User not found", HttpStatus.NOT_FOUND);
//
//   }
//
//   // @desc Edit memo
//// @route PUT /api/memos
//// @access Private
//   @PutMapping
//   public ResponseEntity<Object> updateMemo (@RequestParam Integer userId, @RequestParam int memoId, @RequestParam String content) {
//      if (userRepository.findById(userId).isPresent() && memoRepository.findById(memoId).isPresent()) {
//         User user = userRepository.findById(userId).get();
//         Memo memo = memoRepository.findById(memoId).get();
//         memo.setMemoContent(content);
//         memo.setSanitizedHtml(Utils.parseMarkdown(content));
//         memo.setUpdatedAt(LocalDateTime.now());
//
//         memoRepository.save(memo);
//
//         return new ResponseEntity<Object>(memo,HttpStatus.OK);
//      } else
//         return new ResponseEntity<Object>("Not found", HttpStatus.NOT_FOUND);
//
//   }
//
//   // @desc Delete memo
//// @route DELETE /api/memos
//// @access Private
//   @DeleteMapping
//   public ResponseEntity<Object> deleteMemo (@RequestParam Integer userId, @RequestParam int memoId) {
//      if (userRepository.findById(userId).isPresent() && memoRepository.findById(memoId).isPresent()) {
//         User user = userRepository.findById(userId).get();
//         Memo memo = memoRepository.findById(memoId).get();
//         memoRepository.deleteById(memoId);
//
//         return new ResponseEntity<Object>(memo,HttpStatus.OK);
//      } else
//         return new ResponseEntity<Object>("Not found", HttpStatus.NOT_FOUND);
//
//   }


}
