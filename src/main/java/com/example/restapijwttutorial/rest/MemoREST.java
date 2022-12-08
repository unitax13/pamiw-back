package com.example.restapijwttutorial.rest;


import com.example.restapijwttutorial.document.Memo;
import com.example.restapijwttutorial.document.User;
import com.example.restapijwttutorial.repository.MemoRepository;
import com.example.restapijwttutorial.repository.UserRepository;
import com.example.restapijwttutorial.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(path = "/api/memos")
public class MemoREST {
   @Autowired
   private MemoRepository memoRepository;
   @Autowired
   private UserRepository userRepository;


   @GetMapping
   public ResponseEntity<?> getAll(@RequestParam String userId) {
      if (userRepository.findById(userId).isPresent()) {
         return new ResponseEntity<Object>("Found user", HttpStatus.OK);

      }
      return new ResponseEntity<Object>("Not found", HttpStatus.OK);
   }

   // @desc Add memo
// @route POST /api/memos
// @access Private
   @PostMapping
   public ResponseEntity<?> addMemo(@AuthenticationPrincipal User user, @RequestParam String content) {

         List<Memo> list = user.getMemos();
         if (list == null) {
            list = new ArrayList<>();
         }
         Memo newMemo = new Memo(content);
         newMemo.setSanitizedHtml(Utils.parseMarkdown(content));
         newMemo.setUser(user);
         newMemo.setCreatedAt(LocalDateTime.now());
         newMemo.setUpdatedAt(LocalDateTime.now());


         list.add(newMemo);
         user.setMemos(list);
         memoRepository.save(newMemo);
         userRepository.save(user);

         return new ResponseEntity<Object>(newMemo,HttpStatus.OK);

   }

   // @desc Edit memo
// @route PUT /api/memos
// @access Private
   @PutMapping
   public ResponseEntity<Object> updateMemo(@AuthenticationPrincipal User user, @RequestParam String memoId, @RequestParam String content) {
      if (memoRepository.existsById(memoId)) {

         boolean exists = false;

         List<Memo> newMemos = user.getMemos();

         for (int i=0; i< newMemos.size(); i++) {
            Memo memo = newMemos.get(i);
            if (memo.getId() != null && memo.getId().equals(memoId)) {
               exists = true;
               memo.setMemoContent(content);
               memo.setSanitizedHtml(Utils.parseMarkdown(content));
               memo.setUpdatedAt(LocalDateTime.now());
               memoRepository.save(memo);
               newMemos.set(i,memo);
               user.setMemos(newMemos);
               userRepository.save(user);
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
   public ResponseEntity<Object> deleteMemo(@AuthenticationPrincipal User user, @RequestParam String memoId) {
      if (memoRepository.existsById(memoId)) {

         boolean exists = false;

         List<Memo> newMemos = user.getMemos();

         for (int i=0; i< newMemos.size(); i++) {
            Memo memo = newMemos.get(i);
            if (memo.getId() != null && memo.getId().equals(memoId)) {
               exists = true;
               newMemos.remove(i);
               user.setMemos(newMemos);
               userRepository.save(user);
               memoRepository.deleteById(memoId);
               break;
            }
         }



         return exists ? new ResponseEntity<Object>("exists",HttpStatus.OK) : new ResponseEntity<Object>("yeah, but not on this user",HttpStatus.OK);
      } else {
         return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
      }
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
