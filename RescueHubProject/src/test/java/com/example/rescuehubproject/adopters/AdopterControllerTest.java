package com.example.rescuehubproject.adopters;

import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.adopters.controllers.AdopterController;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.exceptions.UserNotFoundException;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.adopters.services.AdopterService;
import com.example.rescuehubproject.setup.DataInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdopterController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdopterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdopterService adopterService;

    @MockBean
    private DataInitializer dataInitializer;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AdopterRepository adopterRepository;

    @ParameterizedTest
    @WithMockUser(roles = {"USER", "ADMINISTRATOR"})
    @ValueSource(strings = {"USER", "ADMINISTRATOR"})
    public void testGetAllAdopters(String role) throws Exception {

        GetAdopterDTO getAdopterDto = new GetAdopterDTO();
        getAdopterDto.setEmail("test@example.com");
        List<GetAdopterDTO> dtoList = List.of(getAdopterDto);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "email");
        Page<GetAdopterDTO> page = new PageImpl<>(dtoList, pageable, dtoList.size());

        when(adopterService.findAll(pageable, "")).thenReturn(page);

        mockMvc.perform(get("/api/adopters").with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(dtoList.size()))
                .andExpect(jsonPath("$[0].email").value(getAdopterDto.getEmail()));
    }

    @Test
    public void testGetAdopterByIdForbiddenForAdopter() throws Exception {
        Long adopterId = 1L;
        GetAdopterByIdDTO getAdopterByIdDto = new GetAdopterByIdDTO();
        getAdopterByIdDto.setId(adopterId);
        getAdopterByIdDto.setEmail("test@example.com");

        when(adopterService.findById(adopterId)).thenReturn(getAdopterByIdDto);

        mockMvc.perform(get("/api/adopters/" + adopterId))
                .andExpect(status().isUnauthorized()    );

        verify(adopterService, never()).findById(adopterId);
    }

    @ParameterizedTest
    @WithMockUser(roles = {"USER", "ADMIN"})
    @ValueSource(strings = {"USER", "ADMIN"})
    public void testGetAdopterById(String role) throws Exception {
        Long adopterId = 1L;
        GetAdopterByIdDTO getAdopterByIdDto = new GetAdopterByIdDTO();
        getAdopterByIdDto.setId(adopterId);
        getAdopterByIdDto.setEmail("test@example.com");

        when(adopterService.findById(adopterId)).thenReturn(getAdopterByIdDto);

        mockMvc.perform(get("/api/adopters/" + adopterId).with(user("user").roles(role)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(adopterId))
                .andExpect(jsonPath("$.email").value(getAdopterByIdDto.getEmail()));

        verify(adopterService, times(1)).findById(adopterId);
    }




    @ParameterizedTest
    @WithMockUser(roles = {"USER", "ADMIN"})
    @ValueSource(strings = {"USER", "ADMIN"})
    public void testGetAdopterByIdthrowsExceptionWhenUserNotFound(){
        Long adopterId = 1L;
        when(adopterService.findById(adopterId)).thenThrow(new UserNotFoundException());

        try {
            mockMvc.perform(get("/api/adopters/" + adopterId))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}