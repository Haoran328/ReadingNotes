package cn.edu.xjtlu.readingnotes.user.controller;

import org.springframework.http.MediaType;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cn.edu.xjtlu.readingnotes.user.User;
import cn.edu.xjtlu.readingnotes.user.UserRepo;
import cn.edu.xjtlu.readingnotes.util.Role;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    private PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();
    
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String register(@RequestParam Map<String,String> params) {
        final String name = params.get("username");
        final String key = passwordEncoder.encode(params.get("password"));
        final String mail = params.get("email");
        final User user = new User(name, key, mail);
        user.setRole(Role.USER);
        user.setIsNonLocked(false);
        user.setIsEnabled(false);
        userRepo.save(user);
        return "redirect:/login?registered";
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userRepo.findById(id).orElseThrow());
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public String updateUser(
        @PathVariable Long id,
        @RequestParam String oldPassword,
        @RequestBody User user,
        RedirectAttributes redirectAttributes) {
        
        User existing = userRepo.findById(id).orElseThrow();
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, existing.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
            return "redirect:/profile";
        }
    
        // Added field checksum save logic
        if (user.getUsername() != null) {
            existing.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existing.setEmail(user.getEmail());
        existing.setAvatar(user.getAvatar());
        
        userRepo.save(existing);
        redirectAttributes.addFlashAttribute("success", "Password updated");
        return "redirect:/profile";
    } 
}  