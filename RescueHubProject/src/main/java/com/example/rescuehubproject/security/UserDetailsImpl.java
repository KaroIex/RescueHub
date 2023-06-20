package com.example.rescuehubproject.security;

import com.example.rescuehubproject.accounts.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails { // used to work with Spring Security

    private final User user;

    private final Long id;

    private final String username;

    private final String password;

    private final List<GrantedAuthority> authorities;


    public UserDetailsImpl(User user) {
        this.user = user;
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.id = user.getId();
        this.authorities = new ArrayList<>(getAuthorities());
    }

    public UserDetailsImpl(String id, String username, String password, List<Object> emptyList) {
        this.id = Long.valueOf(id);
        this.username = username;
        this.password = password;
        this.authorities = new ArrayList<>(getAuthorities());
        this.user = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoles() {
        //zwróć liste ról
        return authorities.toString();
    }

    public String getId() {
        return id.toString();
    }
}