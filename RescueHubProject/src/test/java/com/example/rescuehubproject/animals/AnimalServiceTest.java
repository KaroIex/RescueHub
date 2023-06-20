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
import org.mockito.ArgumentCaptor;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.times;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
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

    @Test
    void testFindById() throws NoSuchFieldException {
        AnimalSpecies animalSpecies = new AnimalSpecies();
        animalSpecies.setSpeciesName("Kot");
        animalSpecies.setId(1L);

        Animal animal = new Animal();
        animal.setId(1L);
        animal.setName("Test Animal");
        animal.setAge(15);
        animal.setDescription("This is " + animal.getName());
        animal.setSocialAnimal(true);
        animal.setGoodWithChildren(true);
        animal.setNeedsAttention(false);
        animal.setNeedsOutdoorSpace(false);
        animal.setAnimalSpecies(animalSpecies);

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        Optional<AnimalDTO> resultOptional = animalService.findById(1L);

        assertTrue(resultOptional.isPresent());
        assertEquals("Kot", resultOptional.get().getAnimalSpecies());
        assertEquals("Test Animal", resultOptional.get().getName());
    }

    @Test
    void testFindById_IdNotExist() throws NoSuchFieldException {
        when(animalRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<AnimalDTO> resultOptional = animalService.findById(1L);
        assertNull(resultOptional);
    }

    @Test
    public void testSave() throws NoSuchFieldException{
        AnimalSpecies animalSpecies = new AnimalSpecies();
        animalSpecies.setSpeciesName("Kot");

        Animal animal = new Animal();
        animal.setName("Test Animal");
        animal.setAnimalSpecies(animalSpecies);

        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> {
            Animal savedAnimal = invocation.getArgument(0);
            savedAnimal.setId(1L);
            return savedAnimal;
        });
        when(animalSpeciesRepository.findAll()).thenReturn(Collections.singletonList(animalSpecies));

        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setName("Test Animal");
        animalDTO.setAge(15);
        animalDTO.setDescription("This is Test Animal" );
        animalDTO.setSocialAnimal(true);
        animalDTO.setGoodWithChildren(true);
        animalDTO.setNeedsAttention(false);
        animalDTO.setNeedsOutdoorSpace(false);
        animalDTO.setAnimalSpecies("Kot");

        AnimalDTO resultDTO = animalService.save(animalDTO);

        assertEquals("Test Animal", resultDTO.getName());
        assertEquals("Kot", resultDTO.getAnimalSpecies());
    }

    @Test
    void testDeleteById(){
        animalService.deleteById(1L);
    }

    @Test
    public void testDeleteById_EmptyDatabase() {
        Long animalId = 1L;

        when(animalRepository.findById(animalId)).thenReturn(Optional.empty());
        animalService.deleteById(animalId);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(animalRepository).deleteById(idCaptor.capture());

        Long capturedId = idCaptor.getValue();
        assertEquals(animalId, capturedId);
    }
}
