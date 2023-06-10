package com.example.rescuehubproject.adopters;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.controllers.UserController;
import com.example.rescuehubproject.accounts.request.AuthenticationRequest;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.controllers.AdopterController;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.dto.UpdateAdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.services.AdopterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.databind.ObjectMapper;




@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdopterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AdopterController adopterController;

    @Mock
    private AdopterService adopterService;

    @Mock
    private ModelMapper modelMapper;

    private Adopter adopter;
    private GetAdopterDTO getAdopterDTO;
    private GetAdopterByIdDTO getAdopterByIdDTO;
    private UpdateAdopterDTO updateAdopterDTO;

    private GetAdopterByIdDTO adopterByIdDTO;

    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private UserController userController;


    private String setUpUser() {
        User user = new User();
        user.setLastname("user");
        user.setName("user");
        user.setEmail("user@test.com");
        user.setPassword("!QAZXSW@123456");
        user.addRole(Role.ADOPTER);
        userController.registerAccount(user);

        User user2 = new User();
        user2.setLastname("user2");
        user2.setName("user2");
        user2.setEmail("user2@test.com");
        user2.setPassword("!QAZXSW@123456");
        user2.addRole(Role.ADOPTER);
        userController.registerAccount(user2);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("user@test.com");
        authenticationRequest.setPassword("!QAZXSW@123456");

        var jwt = userController.authenticateUser(authenticationRequest);
        return String.valueOf(jwt);
    }

    @BeforeEach
    void setUp() {

        getAdopterDTO = new GetAdopterDTO();
        getAdopterDTO.setId(1L);
        getAdopterDTO.setName("John");

        getAdopterByIdDTO = new GetAdopterByIdDTO();
        getAdopterByIdDTO.setId(1L);
        getAdopterByIdDTO.setName("John");

        updateAdopterDTO = new UpdateAdopterDTO();
        updateAdopterDTO.setName("John");
        updateAdopterDTO.setLastname("Doe");
        updateAdopterDTO.setEmail("john.doe@example.com");
        updateAdopterDTO.setPhone("123456789");

        adopterByIdDTO = new GetAdopterByIdDTO();
        adopterByIdDTO.setId(1L);
    }

    @Test
    @DirtiesContext
    void getAllAdopters_whenAuthorizedAdopter_returns_OK() throws Exception {
        // given a pageable with adopter data
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String direction = "ASC";
        String filter = "";
        GetAdopterDTO user1 = new GetAdopterDTO();
        user1.setId(1L);
        user1.setName("user");

        GetAdopterDTO user2 = new GetAdopterDTO();
        user2.setId(2L);
        user2.setName("user2");

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortBy);
        List<GetAdopterDTO> adopterList = Arrays.asList(
                user1, user2
        );
        Page<GetAdopterDTO> adopterPage = new PageImpl<>(adopterList, pageable, adopterList.size());

        // when
        when(adopterService.findAll(pageable, filter)).thenReturn(adopterPage);

        // then
        mockMvc.perform(get("/api/adopters")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpUser())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sortBy)
                        .param("direction", direction)
                        .param("filter", filter))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAdopters_ReturnsInternalServerError_WhenExceptionIsThrown() {
        when(adopterService.findAll(any(Pageable.class), anyString())).thenThrow(new RuntimeException());

        ResponseEntity<List<GetAdopterDTO>> response = adopterController.getAllAdopters(0, 10, "name", "ASC", "");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void getAdopterById_ReturnsAdopter_WhenFound() {
        when(adopterService.findById(anyLong())).thenReturn(adopterByIdDTO);

        ResponseEntity<GetAdopterByIdDTO> response = adopterController.getAdopterById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(adopterByIdDTO, response.getBody());
    }
}