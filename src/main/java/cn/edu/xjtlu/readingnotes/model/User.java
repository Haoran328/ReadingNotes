package cn.edu.xjtlu.readingnotes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String email;
    
    @Lob
    private byte[] avatar;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
    private boolean active = true;
}

public enum Role {
    USER, ADMIN
}
