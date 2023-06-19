package com.example.rescuehubproject.accounts.controllers;

import com.example.rescuehubproject.accounts.request.UserRegisterDTO;
import com.example.rescuehubproject.accounts.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setName("user");
        userDto.setLastname("user");
        userDto.setEmail("user@test.com");
        userDto.setPassword("!QAZXSW@123456");

        when(userService.registerAccount(userDto)).thenReturn(userDto);

        ResponseEntity<UserRegisterDTO> response = userController.registerAccount(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        assertEquals("user", userDto.getName());
        assertEquals("user", userDto.getLastname());
        assertEquals("user@test.com", userDto.getEmail());
        assertEquals("!QAZXSW@123456", userDto.getPassword());
    }
}