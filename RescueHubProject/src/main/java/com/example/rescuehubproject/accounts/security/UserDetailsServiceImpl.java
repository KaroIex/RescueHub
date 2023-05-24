package com.example.rescuehubproject.accounts.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.rescuehubproject.accounts.execeptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import com.example.rescuehubproject.accounts.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email)
                .map(UserDetailsImpl::new) // converter from UserInfo to UserDetails
                .orElseThrow(UserNotFoundException::new); // if user not found
    }
}