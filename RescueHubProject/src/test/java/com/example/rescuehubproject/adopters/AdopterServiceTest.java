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
//
//    @Mock
//    private AdopterRepository adopterRepository;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private AdopterService adopterService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    private User createUser(String name, String lastname) {
//        User user = new User();
//        user.setName(name);
//        user.setLastname(lastname);
//        return user;
//    }
//
//    @Test
//    public void testFindAll() {
//        // Prepare test data
//        Pageable pageable = Pageable.unpaged();
//        List<User> users = new ArrayList<>();
//        users.add(createUser("John", "Doe"));
//        users.add(createUser("Alice", "Smith"));
//        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
//
//        // Configure mock repository
//        when(adopterRepository.findAll(pageable)).thenReturn(userPage);
//
//        // Configure mock model mapper
//        when(modelMapper.map(any(User.class), eq(GetAdopterDTO.class))).thenReturn(new GetAdopterDTO());
//
//        // Perform the service method
//        Page<GetAdopterDTO> result = adopterService.findAll(pageable, "");
//
//        // Verify the results
//        assertEquals(users.size(), result.getSize());
//        verify(adopterRepository, times(1)).findAll(pageable);
//        verify(modelMapper, times(users.size())).map(any(User.class), eq(GetAdopterDTO.class));
//    }
//
//    @Test
//    public void testFindById() {
//        // Prepare test data
//        Long id = 1L;
//        User user = createUser("John", "Doe");
//        user.addRole(Role.ROLE_ADOPTER);
//        Optional<User> userOptional = Optional.of(user);
//
//        // Configure mock repository
//        when(adopterRepository.findById(id)).thenReturn(userOptional);
//
//        // Configure mock model mapper
//        when(modelMapper.map(user, GetAdopterByIdDTO.class)).thenReturn(new GetAdopterByIdDTO());
//
//        // Perform the service method
//        GetAdopterByIdDTO result = adopterService.findById(id);
//
//        // Verify the results
//        assertEquals(userOptional.isPresent(), result != null);
//        verify(adopterRepository, times(1)).findById(id);
//        verify(modelMapper, times(userOptional.isPresent() ? 1 : 0)).map(user, GetAdopterByIdDTO.class);
//    }
//
//    @Test
//    public void testUpdateAdopter() {
//        // Prepare test data
//        Long id = 1L;
//        UpdateAdopterDTO updateAdopterDTO = new UpdateAdopterDTO();
//        updateAdopterDTO.setName("John");
//        updateAdopterDTO.setLastname("Doe");
//        updateAdopterDTO.setEmail("john.doe@example.com");
//        updateAdopterDTO.setPhone("123456789");
//
//        Adopter adopter = new Adopter();
//        adopter.setId(id);
//        adopter.addRole(Role.ROLE_ADOPTER);
//        adopter.setName("John");
//
//        Optional<User> userOptional = Optional.of(adopter);
//
//        // Configure mock repository
//        when(adopterRepository.findById(id)).thenReturn(userOptional);
//        when(adopterRepository.save(adopter)).thenReturn(adopter);
//
//        // Perform the service method
//        Adopter result = adopterService.updateAdopter(id, updateAdopterDTO);
//
//        // Verify the results
//        assertEquals(userOptional.isPresent(), result != null);
//        assertEquals(updateAdopterDTO.getName(), adopter.getName());
//        assertEquals(updateAdopterDTO.getLastname(), adopter.getLastname());
//        assertEquals(updateAdopterDTO.getEmail(), adopter.getEmail());
//        //assertEquals(updateAdopterDTO.getPhone(), user.getPhone());
//        verify(adopterRepository, times(1)).findById(id);
//        verify(adopterRepository, times(1)).save(adopter);
//    }
//
//    @Test
//    void findAll_ReturnsEmptyPage_WhenNoAdoptersFound() {
//        // Prepare test data
//        Pageable pageable = Pageable.unpaged();
//
//        // Configure mock repository to return an empty page
//        when(adopterRepository.findAll(pageable)).thenReturn(Page.empty());
//
//        // Perform the service method
//        Page<GetAdopterDTO> result = adopterService.findAll(pageable, "");
//
//        // Verify the result is an empty page
//        assertTrue(result.isEmpty());
//        verify(adopterRepository, times(1)).findAll(pageable);
//        verifyNoInteractions(modelMapper);
//    }
//
//    @Test
//    void findById_ReturnsNull_WhenAdopterNotFound() {
//        // Prepare test data
//        Long id = 1L;
//
//        // Configure mock repository to return an empty optional
//        when(adopterRepository.findById(id)).thenReturn(Optional.empty());
//
//        // Perform the service method
//        GetAdopterByIdDTO result = adopterService.findById(id);
//
//        // Verify the result is null
//        assertNull(result);
//        verify(adopterRepository, times(1)).findById(id);
//        verifyNoInteractions(modelMapper);
//    }
//
//    @Test
//    void updateAdopter_ReturnsNull_WhenAdopterNotFound() {
//        // Prepare test data
//        Long id = 1L;
//        UpdateAdopterDTO updateAdopterDTO = new UpdateAdopterDTO();
//
//        // Configure mock repository to return an empty optional
//        when(adopterRepository.findById(id)).thenReturn(Optional.empty());
//
//        // Perform the service method
//        Adopter result = adopterService.updateAdopter(id, updateAdopterDTO);
//
//        // Verify the result is null
//        assertNull(result);
//        verify(adopterRepository, times(1)).findById(id);
//        verifyNoMoreInteractions(adopterRepository);
//    }
}