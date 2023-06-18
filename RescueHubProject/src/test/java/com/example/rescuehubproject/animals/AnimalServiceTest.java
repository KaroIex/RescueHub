package com.example.rescuehubproject.animals;

import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.adopters.services.AdopterService;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.dto.AnimalsWithIdDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import com.example.rescuehubproject.animals.services.AnimalService;
import com.example.rescuehubproject.animals.services.AnimalSpeciesService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AnimalServiceTest {

    @Autowired
    AnimalService animalService;

    @MockBean
    private DataInitializer dataInitializer;

    @MockBean
    Authentication authentication;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AnimalSpeciesRepository animalSpeciesRepository;

    @MockBean
    AnimalRepository animalRepository;

    @Test
    public void testFindAll(){

        AnimalSpecies animalSpecies = new AnimalSpecies();
        animalSpecies.setSpeciesName("Kot");
        animalSpecies.setId(1L);

        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Jim");
        animal.setAge(15);
        animal.setDescription("This is " + animal.getName());
        animal.setSocialAnimal(true);
        animal.setGoodWithChildren(true);
        animal.setNeedsAttention(false);
        animal.setNeedsOutdoorSpace(false);
        animal.setAnimalSpecies(animalSpecies);


        Animal animal2 = new Animal();
        animal2.setId(2L);
        animal2.setName("Fix");
        animal2.setAge(2);
        animal2.setDescription("This is " + animal.getName());
        animal2.setSocialAnimal(true);
        animal2.setGoodWithChildren(false);
        animal2.setNeedsAttention(false);
        animal2.setNeedsOutdoorSpace(true);
        animal2.setAnimalSpecies(animalSpecies);

        List<Animal> animalList = Arrays.asList(animal, animal2);

        when(animalRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(new PageImpl<>(animalList,  PageRequest.of(0, 10), 1));

        Page<AnimalsWithIdDTO> resultPage = animalService.findAll(PageRequest.of(0, 10), "");

        assertEquals(2, resultPage.getTotalElements());
        assertEquals(1L, resultPage.getContent().get(0).getId());
        assertEquals(2L, resultPage.getContent().get(1).getId());

    }
}
