package com.example.rescuehubproject.animals.services;

import com.example.rescuehubproject.animals.dto.AnimalSpeciesDTO;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalSpeciesService {

    @Autowired
    private AnimalSpeciesRepository animalSpeciesRepository;

    //dopisz metody do serwisu
    //- konwersja z entity na dto i odwrotnie


    public List<AnimalSpeciesDTO> findAll() {
        return animalSpeciesRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AnimalSpeciesDTO> findById(Long id) {
        return animalSpeciesRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AnimalSpeciesDTO save(AnimalSpeciesDTO animalSpeciesDTO) {
        AnimalSpecies animalSpecies = convertToEntity(animalSpeciesDTO);
        return convertToDTO(animalSpeciesRepository.save(animalSpecies));
    }

    public void deleteById(Long id) {
        animalSpeciesRepository.deleteById(id);
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