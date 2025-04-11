package cn.edu.xjtlu.readingnotes.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.edu.xjtlu.readingnotes.user.User;

public class UserInfo implements UserDetails {

    private Long id;
    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private boolean nonLocked;
    private boolean enabled;

    public UserInfo(Long id, String username, String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean nonLocked, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.nonLocked = nonLocked;
        this.enabled = enabled;
    }

    public static UserInfo build(User user) {
        List<GrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority(user.getRole().name()));

        return new UserInfo(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            authorities,
            user.getIsNonLocked(),
            user.getIsEnabled());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public boolean isNonLocked() {
        return nonLocked;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
