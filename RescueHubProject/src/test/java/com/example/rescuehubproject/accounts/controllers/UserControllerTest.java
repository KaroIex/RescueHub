package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.services.UserService;
import com.example.rescuehubproject.accounts.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, null, null);
    }


    @Test
    void registerAccount() {
        User user = new User();
        user.setName("user");
        user.setLastname("user");
        user.setEmail("user@test.com");
        user.setPassword("!QAZXSW@123456");
        user.addRole(Role.USER);

        when(userService.registerAccount(user)).thenReturn(ResponseEntity.ok(user));

        ResponseEntity<User> response = userController.registerAccount(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        //check ig user has name User
        assertEquals("user", user.getName());
        //check if user has lastname User
        assertEquals("user", user.getLastname());
        //check if user has email user@test.com
        assertEquals("user@test.com", user.getEmail());
        //check if user has password !QAZXSW@123456
        assertEquals("!QAZXSW@123456", user.getPassword());
        //check if user has role USER
        assertTrue(user.getRoles().contains(Role.USER));

    }
}