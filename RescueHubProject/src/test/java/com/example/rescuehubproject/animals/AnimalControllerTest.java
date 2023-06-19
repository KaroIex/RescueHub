package com.example.rescuehubproject.animals;

import com.example.rescuehubproject.animals.controllers.AnimalController;
import com.example.rescuehubproject.animals.dto.AnimalsWithIdDTO;
import com.example.rescuehubproject.animals.services.AnimalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AnimalController.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

}
