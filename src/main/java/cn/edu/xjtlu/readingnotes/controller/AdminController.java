package cn.edu.xjtlu.readingnotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.xjtlu.readingnotes.model.User;
import cn.edu.xjtlu.readingnotes.repository.UserRepo;

@RestController
public class AdminController {

    @Autowired
    private UserRepo userRepo;

    @PutMapping("/user/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        user.setIsActive(false);
        return ResponseEntity.ok(userRepo.save(user));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    @PutMapping("/user/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(
        @PathVariable Long id, 
        @RequestParam boolean active) {
        User user = userRepo.findById(id).orElseThrow();
        user.setIsActive(active);
        return ResponseEntity.ok(userRepo.save(user));
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userRepo.deleteById(id);
    }
}