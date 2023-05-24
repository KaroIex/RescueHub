package com.example.rescuehubproject.accounts.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.execeptions.PasswordMustBeAtLeast12Chars;
import com.example.rescuehubproject.accounts.execeptions.PasswordMustBeDifferentException;
import com.example.rescuehubproject.accounts.execeptions.UserExistException;
import com.example.rescuehubproject.accounts.pojo.ChangePass;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.responses.PasswordChanged;
import com.example.rescuehubproject.accounts.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService { // service for user registration
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public ResponseEntity<User> registerAccount(User user) {

        if (userRepository.existsUserByEmailIgnoreCase(user.getEmail())) { // check if user with this email already exists
            System.out.println("user with this email already exists");
            throw new UserExistException();
        }

        if (user.getPassword().length() < 12) { // check if password is at least 12 chars
            System.out.println("password must be at least 12 chars");
            throw new PasswordMustBeAtLeast12Chars();
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(user); // return user with id
    }

    public ResponseEntity<PasswordChanged> changePassword(UserDetailsImpl userDetails, ChangePass changePass) { // change user password
        Optional<User> user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());

        if (changePass.getNew_password().length() < 12){
            System.out.println("password must be at least 12 chars");
            throw new PasswordMustBeAtLeast12Chars();
        }

        if (encoder.matches(changePass.getNew_password(), user.get().getPassword())) {
            System.out.println("password must be different");
            throw new PasswordMustBeDifferentException();
        }

        user.get().setPassword(encoder.encode(changePass.getNew_password())); // set new password
        userRepository.save(user.get()); // save changes
//        return ResponseEntity.ok(user.get()); // return user with id

        return ResponseEntity.ok(new PasswordChanged(user.get().getEmail().toLowerCase()));


    }

}
