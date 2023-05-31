package com.example.rescuehubproject.animals.services;

import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<AnimalDTO> findAll(){
        List<Animal> animals = animalRepository.findAll();
        return animals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AnimalDTO> findById(Long id){
        Optional<Animal> animal = animalRepository.findById(id);
        return animal.map(this::convertToDTO);
    }

    public AnimalDTO save(AnimalDTO animalDTO){
        Animal animal = convertToEntity(animalDTO);
        return convertToDTO(animalRepository.save(animal));
    }

    public void deleteById(Long id){
        animalRepository.deleteById(id);
    }

    private AnimalDTO convertToDTO(Animal animal) {
        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setName(animal.getName());
        return animalDTO;
    }

    private Animal convertToEntity(AnimalDTO animalDTO) {
        Animal animal = new Animal();
        animal.setName(animalDTO.getName());
        return animal;
    }
}
