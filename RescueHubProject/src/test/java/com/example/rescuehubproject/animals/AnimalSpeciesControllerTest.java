package com.example.rescuehubproject.animals;

import com.example.rescuehubproject.accounts.controllers.UserController;
import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.request.AuthenticationRequest;
import com.example.rescuehubproject.accounts.services.UserService;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.animals.controllers.AnimalSpeciesController;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesWithIdDTO;
import com.example.rescuehubproject.animals.services.AnimalSpeciesService;
import com.example.rescuehubproject.security.JwtUtils;
import com.example.rescuehubproject.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.http.HttpHeaders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesWithIdDTO;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AnimalSpeciesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AnimalSpeciesService animalSpeciesService;

    @Autowired
    private UserService userAccountService;
    @Autowired
    private UserController userController;


    private String setUpAdmin(){
        User admin = new User();
        admin.setLastname("admin");
        admin.setName("admin");
        admin.setEmail("admin@test.com");
        admin.setPassword("!QAZXSW@123456");
        admin.addRole(Role.ADMINISTRATOR);
        userController.registerAccount(admin);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("admin@test.com");
        authenticationRequest.setPassword("!QAZXSW@123456");

        var jwt = userController.authenticateUser(authenticationRequest);
        return String.valueOf(jwt);
    }

    @Test
    @DirtiesContext
    void findAll_whenAuthorizedAdmin_returns_OK() throws Exception {
        // given a pageable with animal species data
        int page = 0;
        int size = 10;
        String sortBy = "speciesName";
        String direction = "ASC";
        String filter = "";

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortBy);
        List<AnimalSpeciesWithIdDTO> animalSpeciesList = Arrays.asList(
                new AnimalSpeciesWithIdDTO(1L, "Dog"),
                new AnimalSpeciesWithIdDTO(2L, "Cat")
        );
        Page<AnimalSpeciesWithIdDTO> animalSpeciesPage = new PageImpl<>(animalSpeciesList, pageable, animalSpeciesList.size());

        // when
        when(animalSpeciesService.findAll(pageable, filter)).thenReturn(animalSpeciesPage);

        // then
        mockMvc.perform(get("/api/animalspecies")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpAdmin())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sortBy)
                        .param("direction", direction)
                        .param("filter", filter))
                .andExpect(status().isOk());
    }
    @Test
    @DirtiesContext
    void findById_whenAuthorizedAdmin_returnsOK() throws Exception {
        // given an existing animal species
        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);
        Long id = 1L;

        // when
        when(animalSpeciesService.findById(id)).thenReturn(Optional.of(species));

        // then
        mockMvc.perform(get("/api/animalspecies/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void save_whenAuthorizedAdmin_returnsOK() throws Exception {
        // given
        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);

        // when
        when(animalSpeciesService.save(species)).thenReturn(species);

        // then
        mockMvc.perform(post("/api/animalspecies")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(species)))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void update_whenAuthorizedAdmin_returnsOK() throws Exception {
        // given an existing animal species
        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);
        Long id = 1L;

        // when
        when(animalSpeciesService.findById(id)).thenReturn(Optional.of(species));
        when(animalSpeciesService.update(id, species)).thenReturn(species);

        // then
        mockMvc.perform(put("/api/animalspecies/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(species)))
                .andExpect(status().isOk());
    }


    @Test
    @DirtiesContext
    void delete_whenAuthorizedAdmin_returnsNoContent() throws Exception {
        // given an existing animal species
        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);
        Long id = 1L;

        // when
        when(animalSpeciesService.findById(id)).thenReturn(Optional.of(species));

        // Delete the created animal species
        mockMvc.perform(delete("/api/animalspecies/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + setUpAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    void findAll_whenUnauthorizedUser_returnsOK() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "speciesName";
        String direction = "ASC";
        String filter = "";

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(direction), sortBy);
        List<AnimalSpeciesWithIdDTO> animalSpeciesList = Arrays.asList(
                new AnimalSpeciesWithIdDTO(1L, "Dog"),
                new AnimalSpeciesWithIdDTO(2L, "Cat")
        );
        Page<AnimalSpeciesWithIdDTO> animalSpeciesPage = new PageImpl<>(animalSpeciesList, pageable, animalSpeciesList.size());

        // when
        when(animalSpeciesService.findAll(pageable, filter)).thenReturn(animalSpeciesPage);

        // then
        mockMvc.perform(get("/api/animalspecies")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sortBy)
                        .param("direction", direction)
                        .param("filter", filter))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void findById_whenUnauthorizedUser_returnsOK() throws Exception {
        // given an existing animal species
        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);
        Long id = 1L;

        // when
        when(animalSpeciesService.findById(id)).thenReturn(Optional.of(species));

        // then
        mockMvc.perform(get("/api/animalspecies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    public void save_whenUnauthorizedUser_returnsUnauthorized() throws Exception {
        // given

        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);

        // then
        mockMvc.perform(post("/api/animalspecies")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(species)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    void update_whenUnauthorizedUser_returnsUnauthorized() throws Exception {
        // given an existing animal species
        AnimalSpeciesDTO species = objectMapper.readValue("{\"speciesName\": \"Dog\"}", AnimalSpeciesDTO.class);
        Long id = 1L;

        // then
        mockMvc.perform(put("/api/animalspecies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(species)))
                .andExpect(status().isUnauthorized());
    }

}