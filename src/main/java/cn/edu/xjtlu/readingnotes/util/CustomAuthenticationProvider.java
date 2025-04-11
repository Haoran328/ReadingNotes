package cn.edu.xjtlu.readingnotes.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import cn.edu.xjtlu.readingnotes.user.User;
import cn.edu.xjtlu.readingnotes.user.UserRepo;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepo repo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final User user = repo.findByUsername(name).orElseThrow(() -> new UsernameNotFoundException(name));
        final UserInfo principal = UserInfo.build(user);
        return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication
            .equals(UsernamePasswordAuthenticationToken.class);
    }

}
