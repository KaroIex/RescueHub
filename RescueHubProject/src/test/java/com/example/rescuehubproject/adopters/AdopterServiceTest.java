package com.example.rescuehubproject.adopters;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.dto.PutAdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.services.AdopterService;
import com.example.rescuehubproject.security.UserDetailsImpl;
import com.example.rescuehubproject.setup.DataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdopterServiceTest {

    @Autowired
    AdopterService adopterService;

    @MockBean
    private DataInitializer dataInitializer;

    @MockBean
    Authentication authentication;

    @MockBean
    UserRepository userRepository;

    @Test
    public void testFindAll() {
        User user = new User();
        user.setEmail("test@example.com");
        user.addRole(Role.ROLE_ADOPTER);

        Page<User> page = new PageImpl<>(List.of(user), PageRequest.of(0, 10), 1);

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<GetAdopterDTO> adopters = adopterService.findAll(PageRequest.of(0, 10), "");
        assertEquals(1, adopters.getTotalElements());
        assertEquals("test@example.com", adopters.getContent().get(0).getEmail());
    }

    @Test
    public void testFindById_ExistingUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.addRole(Role.ROLE_ADOPTER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        GetAdopterByIdDTO foundAdopter = adopterService.findById(1L);

        assertNotNull(foundAdopter);
        assertEquals(1L, foundAdopter.getId());
        assertEquals("test@example.com", foundAdopter.getEmail());
    }

    @Test
    public void testFindById_NonExistentUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        GetAdopterByIdDTO foundAdopter = adopterService.findById(1L);

        assertNull(foundAdopter);
    }

    @Test
    public void testUpdateAdopter() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.addRole(Role.ROLE_ADOPTER);
        Adopter adopter = new Adopter();
        user.setAdopter(adopter);

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        PutAdopterDTO putAdopterDto = new PutAdopterDTO();
        putAdopterDto.setPhone("1234567890");

        PutAdopterDTO updatedAdopter = adopterService.updateAdopter(authentication, putAdopterDto);

        assertEquals("1234567890", updatedAdopter.getPhone());
    }
}


