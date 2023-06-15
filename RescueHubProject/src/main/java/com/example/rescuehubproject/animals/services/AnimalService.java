package com.example.rescuehubproject.animals.services;

import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalSpeciesRepository animalSpeciesRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository) {

        this.animalRepository = animalRepository;

        this.animalSpeciesRepository = animalSpeciesRepository;
    }

    @Transactional
    public List<AnimalDTO> findAll(){
        List<Animal> animals = animalRepository.findAll();
        return animals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public Optional<AnimalDTO> findById(Long id){
        Optional<Animal> animal = animalRepository.findById(id);
        return animal.map(this::convertToDTO);
    }

    @Transactional
    public AnimalDTO save(AnimalDTO animalDTO) throws NoSuchFieldException {
        Animal animal = new Animal();
        animal.setAge(animalDTO.getAge());
        animal.setName(animalDTO.getName());
        animal.setDescription(animalDTO.getDescription());
        animal.setSocialAnimal(animalDTO.isSocialAnimal());
        animal.setNeedsAttention(animalDTO.isNeedsAttention());
        animal.setNeedsOutdoorSpace(animalDTO.isNeedsOutdoorSpace());
        animal.setGoodWithChildren(animalDTO.isGoodWithChildren());

        List<AnimalSpecies> animalSpecies = animalSpeciesRepository.findAll();
        for(AnimalSpecies as: animalSpecies){
            if (Objects.equals(as.getSpeciesName(),animalDTO.getAnimalSpecies())) {
                animal.setAnimalSpecies(as);
                as.addAnimal(animal);
                Animal savedAnimal = animalRepository.save(animal);
                return convertToDTO(savedAnimal);
            }
        }
        throw new NoSuchFieldException("AnimalSpecies with name " + animalDTO.getAnimalSpecies() + " not found");
    }

    @Transactional
    public void deleteById(Long id){
        animalRepository.deleteById(id);
    }

    private AnimalDTO convertToDTO(Animal animal) {
        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setName(animal.getName());
        animalDTO.setAge(animal.getAge());
        animalDTO.setAnimalSpecies(animal.getAnimalSpecies().getSpeciesName());
        animalDTO.setDescription(animal.getDescription());
        animalDTO.setSocialAnimal(animal.isSocialAnimal());
        animalDTO.setNeedsAttention(animal.isNeedsAttention());
        animalDTO.setNeedsOutdoorSpace(animal.isNeedsOutdoorSpace());
        animal.setGoodWithChildren(animalDTO.isGoodWithChildren());
        return animalDTO;
    }

    private Animal convertToEntity(AnimalDTO animalDTO) {
        Animal animal = new Animal();
        animal.setName(animalDTO.getName());
        animal.setAge(animalDTO.getAge());
        animal.setDescription(animalDTO.getDescription());

        List<AnimalSpecies> animalSpecies = animalSpeciesRepository.findAll();
        for(AnimalSpecies as: animalSpecies) {
            if (Objects.equals(as.getSpeciesName(), animalDTO.getAnimalSpecies())) {
                animal.setAnimalSpecies(as);
            }
        }
        return animal;
    }

    public AnimalDTO update(Long id, AnimalDTO updatedAnimalDTO) throws NoSuchFieldException {
        Optional<Animal> exsistingAnimalOptional = animalRepository.findById(id);

        if (exsistingAnimalOptional.isPresent()) {
            Animal exsistingAnimal = exsistingAnimalOptional.get();
            Animal exsistingAnimalOld = exsistingAnimalOptional.get();

            exsistingAnimal.setAge(updatedAnimalDTO.getAge());
            exsistingAnimal.setName(updatedAnimalDTO.getName());
            exsistingAnimal.setDescription(updatedAnimalDTO.getDescription());

            List<AnimalSpecies> animalSpecies = animalSpeciesRepository.findAll();
            for (AnimalSpecies as : animalSpecies) {
                if (Objects.equals(as.getSpeciesName(), updatedAnimalDTO.getAnimalSpecies())) {
                    as.removeAnimal(exsistingAnimalOld);
                    as.addAnimal(exsistingAnimal);
                    exsistingAnimal.setAnimalSpecies(as);

                    Animal updatedAnimal = animalRepository.save(exsistingAnimal);
                    return convertToDTO(updatedAnimal);
                }
            }
        }
        throw new NoSuchFieldException("Animal with id not found or AnimalSpieces");
    }
}
