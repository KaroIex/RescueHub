package com.example.rescuehubproject.animals;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesWithIdDTO;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import com.example.rescuehubproject.animals.services.AnimalSpeciesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AnimalSpeciesServiceTest {

    @InjectMocks
    private AnimalSpeciesService animalSpeciesService;

    @Mock
    private AnimalSpeciesRepository animalSpeciesRepository;



    private AnimalSpecies species1;
    private AnimalSpecies species2;
    private AnimalSpeciesDTO species1DTO;

    @BeforeEach
    void setUp() {
        species1 = new AnimalSpecies();
        species1.setId(1L);
        species1.setSpeciesName("Dog");

        species2 = new AnimalSpecies();
        species2.setId(2L);
        species2.setSpeciesName("Cat");

        species1DTO = new AnimalSpeciesDTO();
        species1DTO.setSpeciesName("Dog");

        AnimalSpeciesDTO species2DTO = new AnimalSpeciesDTO();
        species2DTO.setSpeciesName("Cat");

        AnimalSpeciesWithIdDTO species1WithIdDTO = new AnimalSpeciesWithIdDTO();
        species1WithIdDTO.setId(1L);
        species1WithIdDTO.setSpeciesName("Dog");

        AnimalSpeciesWithIdDTO species2WithIdDTO = new AnimalSpeciesWithIdDTO();
        species2WithIdDTO.setId(2L);
        species2WithIdDTO.setSpeciesName("Cat");
    }

    @Test
    void findAll() {
        List<AnimalSpecies> speciesList = new ArrayList<>();
        speciesList.add(species1);
        speciesList.add(species2);
        Page<AnimalSpecies> speciesPage = new PageImpl<>(speciesList);

        when(animalSpeciesRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(speciesPage);

        PageRequest pageable = PageRequest.of(0, 10);
        Page<AnimalSpeciesWithIdDTO> result = animalSpeciesService.findAll(pageable, "");

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(species1.getId());
        assertThat(result.getContent().get(1).getId()).isEqualTo(species2.getId());
    }

    @Test
    void findById() {
        given(animalSpeciesRepository.findById(1L)).willReturn(Optional.of(species1));

        Optional<AnimalSpeciesDTO> result = animalSpeciesService.findById(1L);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getSpeciesName()).isEqualTo(species1.getSpeciesName());
    }

    @Test
    void save() {
        when(animalSpeciesRepository.save(any())).thenReturn(species1);

        AnimalSpeciesDTO result = animalSpeciesService.save(species1DTO);

        assertThat(result.getSpeciesName()).isEqualTo(species1DTO.getSpeciesName());
    }

    @Test
    void deleteById() {
        given(animalSpeciesRepository.findById(1L)).willReturn(Optional.of(species1));

        animalSpeciesService.deleteById(1L);

        verify(animalSpeciesRepository).deleteById(1L);
    }
}