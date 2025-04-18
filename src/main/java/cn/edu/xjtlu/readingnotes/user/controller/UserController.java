package cn.edu.xjtlu.readingnotes.user.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.edu.xjtlu.readingnotes.user.User;
import cn.edu.xjtlu.readingnotes.user.UserRepo;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userRepo.findById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(
        @PathVariable Long id,
        @RequestParam String username,
        @RequestParam String oldPassword,
        @RequestParam String password,
        @RequestParam String email) {
        
        User existing = userRepo.findById(id).orElseThrow();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/profile"));
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, existing.getPassword())) {
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        User user = new User(username, password, email);
    
        // Added field checksum save logic
        if (!username.isBlank()) {
            existing.setUsername(username);
        }
        if (!password.isBlank()) {
            existing.setPassword(passwordEncoder.encode(password));
        }
        if (!email.isBlank()) {
            existing.setEmail(email);
        }
        existing.setAvatar(user.getAvatar());
        
        userRepo.save(existing);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    } 
}  