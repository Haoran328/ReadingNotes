package cn.edu.xjtlu.readingnotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.xjtlu.readingnotes.model.User;
import cn.edu.xjtlu.readingnotes.repository.UserRepo;
import cn.edu.xjtlu.readingnotes.util.Role;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return ResponseEntity.ok(userRepo.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userRepo.findById(id).orElseThrow());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String token = jwtUtil.generateToken(auth);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody User user) {
        
        User existing = userRepo.findById(id).orElseThrow();
        
        // 新增字段校验
        if (user.getUsername() != null) {
            existing.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existing.setEmail(user.getEmail());
        existing.setAvatar(user.getAvatar());
        return ResponseEntity.ok(userRepo.save(existing));
    }
}