package com.example.rescuehubproject.adopters;

import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.dto.UpdateAdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.adopters.services.AdopterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestPropertySource(locations = "classpath:application-test.properties")
public class AdopterServiceTest {

    @Mock
    private AdopterRepository adopterRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdopterService adopterService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private User createUser(String name, String lastname) {
        User user = new User();
        user.setName(name);
        user.setLastname(lastname);
        return user;
    }

    @Test
    public void testFindAll() {
        // Prepare test data
        Pageable pageable = Pageable.unpaged();
        List<User> users = new ArrayList<>();
        users.add(createUser("John", "Doe"));
        users.add(createUser("Alice", "Smith"));
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        // Configure mock repository
        when(adopterRepository.findAll(pageable)).thenReturn(userPage);

        // Configure mock model mapper
        when(modelMapper.map(any(User.class), eq(GetAdopterDTO.class))).thenReturn(new GetAdopterDTO());

        // Perform the service method
        Page<GetAdopterDTO> result = adopterService.findAll(pageable, "");

        // Verify the results
        assertEquals(users.size(), result.getSize());
        verify(adopterRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(users.size())).map(any(User.class), eq(GetAdopterDTO.class));
    }
}