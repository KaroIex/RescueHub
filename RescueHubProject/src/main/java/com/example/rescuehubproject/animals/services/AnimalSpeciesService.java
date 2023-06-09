package com.example.rescuehubproject.animals.services;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesWithIdDTO;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AnimalSpeciesService {

    @Autowired
    private AnimalSpeciesRepository animalSpeciesRepository;


    public Page<AnimalSpeciesWithIdDTO> findAll(Pageable pageable, String filter) {
        Specification<AnimalSpecies> spec = createSpecification(filter);
        return animalSpeciesRepository.findAll(spec, pageable)
                .map(this::convertToDTOWithId);
    }

    public Optional<AnimalSpeciesDTO> findById(Long id) {
        return animalSpeciesRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AnimalSpeciesDTO save(AnimalSpeciesDTO animalSpeciesDTO) {
        AnimalSpecies animalSpecies = new AnimalSpecies();
        animalSpecies.setSpeciesName(animalSpeciesDTO.getSpeciesName());
        AnimalSpecies savedAnimalSpecies = animalSpeciesRepository.save(animalSpecies);
        return convertToDTO(savedAnimalSpecies);
    }

    public AnimalSpeciesDTO update(Long id, AnimalSpeciesDTO updatedAnimalSpeciesDTO) {
        Optional<AnimalSpecies> existingAnimalSpeciesOptional = animalSpeciesRepository.findById(id);

        if (existingAnimalSpeciesOptional.isPresent()) {
            AnimalSpecies existingAnimalSpecies = existingAnimalSpeciesOptional.get();
            existingAnimalSpecies.setSpeciesName(updatedAnimalSpeciesDTO.getSpeciesName());

            AnimalSpecies updatedAnimalSpecies = animalSpeciesRepository.save(existingAnimalSpecies);
            return convertToDTO(updatedAnimalSpecies);
        } else {
            // Throw an exception or return null if the animal species is not found
            throw new NoSuchElementException("Animal species with id " + id + " not found");
        }
    }


    public void deleteById(Long id) {
        animalSpeciesRepository.deleteById(id);
    }


    private AnimalSpeciesWithIdDTO convertToDTOWithId(AnimalSpecies animalSpecies) {
        AnimalSpeciesWithIdDTO animalSpeciesWithIdDTO = new AnimalSpeciesWithIdDTO();
        animalSpeciesWithIdDTO.setId(animalSpecies.getId());
        animalSpeciesWithIdDTO.setSpeciesName(animalSpecies.getSpeciesName());
        return animalSpeciesWithIdDTO;
    }

    private Specification<AnimalSpecies> createSpecification(String filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null || filter.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("speciesName")), "%" + filter.toLowerCase() + "%");
        };
    }


    private AnimalSpeciesDTO convertToDTO(AnimalSpecies animalSpecies) {
        AnimalSpeciesDTO animalSpeciesDTO = new AnimalSpeciesDTO();
        animalSpeciesDTO.setSpeciesName(animalSpecies.getSpeciesName());
        return animalSpeciesDTO;
    }

    private AnimalSpecies convertToEntity(AnimalSpeciesDTO animalSpeciesDTO) {
        AnimalSpecies animalSpecies = new AnimalSpecies();
        animalSpecies.setSpeciesName(animalSpeciesDTO.getSpeciesName());
        return animalSpecies;
    }
}