package com.example.rescuehubproject.animals;

import com.example.rescuehubproject.animals.controllers.AnimalSpeciesController;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@WebMvcTest(AnimalSpeciesController.class)
public class AnimalSpeciesControllerTest {


}
