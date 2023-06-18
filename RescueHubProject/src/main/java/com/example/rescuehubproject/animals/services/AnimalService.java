package com.example.rescuehubproject.animals.services;

import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.dto.AnimalSpeciesWithIdDTO;
import com.example.rescuehubproject.animals.dto.AnimalsWithIdDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<AnimalsWithIdDTO> findAll(Pageable pageable, String filter){
        Specification<Animal> spec = createSpecification(filter);
        return animalRepository.findAll(spec, pageable)
                .map(this::convertToDTOWithId);
    }

    private AnimalsWithIdDTO convertToDTOWithId(Animal animal) {
        AnimalsWithIdDTO animalsWithIdDTO = new AnimalsWithIdDTO();
        animalsWithIdDTO.setId(animal.getId());
        animalsWithIdDTO.setName(animal.getName());
        animalsWithIdDTO.setAnimalSpecies(animal.getAnimalSpecies().getSpeciesName());
        animalsWithIdDTO.setAge(animal.getAge());
        animalsWithIdDTO.setSocialAnimal(animal.isSocialAnimal());
        animalsWithIdDTO.setDescription(animal.getDescription());
        animalsWithIdDTO.setGoodWithChildren(animal.isGoodWithChildren());
        animalsWithIdDTO.setNeedsAttention(animal.isNeedsAttention());
        animalsWithIdDTO.setNeedsOutdoorSpace(animal.isNeedsOutdoorSpace());
        return animalsWithIdDTO;
    }
    private Specification<Animal> createSpecification(String filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null || filter.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.toLowerCase() + "%");
        };
    }
    @Transactional
    public Optional<AnimalDTO> findById(Long id) throws NoSuchFieldException {
        Optional<Animal> animal = animalRepository.findById(id);
        if(animal.isPresent())
         return animal.map(this::convertToDTO);
        return null;
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
               // as.addAnimal(animal);
                Animal savedAnimal = animalRepository.save(animal);
                return convertToDTO(savedAnimal);
            } return null;
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
        animalDTO.setGoodWithChildren(animal.isGoodWithChildren());
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
