package com.example.rescuehubproject.adoption.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.exceptions.AnimalAlreadyAdoptedException;
import com.example.rescuehubproject.adopters.exceptions.AnimalNotFoundException;
import com.example.rescuehubproject.adopters.exceptions.UserIsNotAnAdopter;
import com.example.rescuehubproject.adopters.exceptions.UserNotFoundException;
import com.example.rescuehubproject.adoption.dto.AdoptionDTO;
import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.example.rescuehubproject.adoption.repositories.AdoptionRepository;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final List<AnimalMatchingAlgorithm> algorithms;
    private final ModelMapper modelMapper;

    @Autowired
    public AdoptionService(AdoptionRepository adoptionRepository, UserRepository userRepository, AnimalRepository animalRepository, List<AnimalMatchingAlgorithm> algorithms, ModelMapper modelMapper) {
        this.adoptionRepository = adoptionRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.algorithms = algorithms;
        this.modelMapper = modelMapper;
    }


    public List<AdoptionDTO> findAll() {
        List<Adoption> adoptions = adoptionRepository.findAll();
        return adoptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AdoptionDTO> findById(Long id) {
        Optional<Adoption> adoption = adoptionRepository.findById(id);
        return adoption.map(this::convertToDTO);
    }

    public AdoptionDTO save(AdoptionDTO adoptionDTO) {
        Adoption adoption = convertToEntity(adoptionDTO);
        Adoption savedAdoption = adoptionRepository.save(adoption);
        return convertToDTO(savedAdoption);
    }

    public void deleteById(Long id) {
        adoptionRepository.deleteById(id);
    }

    private AdoptionDTO convertToDTO(Adoption adoption) {
        AdoptionDTO adoptionDTO = new AdoptionDTO();
        //adoptionDTO.setId(adoption.getId());
        // Mapowanie pól specyficznych dla adopcji
        return adoptionDTO;
    }

    private Adoption convertToEntity(AdoptionDTO adoptionDTO) {
        Adoption adoption = new Adoption();
        //adoption.setId(adoptionDTO.getId());
        // Mapowanie pól specyficznych dla adopcji
        return adoption;
    }

    public Adoption adopt(Authentication authentication, Long animalId) {


        String userId = null;

        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userId = userDetails.getId();
        }
        else { throw new UserNotFoundException(); }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        Optional<Animal> animalOptional = animalRepository.findById(animalId);

        if (!userOptional.isPresent()) throw new UserNotFoundException();
        if (!animalOptional.isPresent()) throw new AnimalNotFoundException();

        User user = userOptional.get();
        Animal animal = animalOptional.get();

        if (!user.getRoles().contains(Role.ROLE_ADOPTER)) throw new UserIsNotAnAdopter();
        if (animal.getAdoptions().size() > 0) throw new AnimalAlreadyAdoptedException();

        Adopter adopter = user.getAdopter();
        Adoption adoption = new Adoption();

        adoption.setAdoptionDate(LocalDate.now());
        adoption.setAdopter(adopter);
        adoption.setAnimal(animal);
        adoptionRepository.save(adoption);

        animal.addAdoption(adoption);
        animalRepository.save(animal);

        return adoption;
    }


    public Page<Map.Entry<AnimalDTO, Double>> matchAdopterToAnimal(AdoptionFormDTO form, Pageable pageable) {

        List<Animal> animals = animalRepository.findAll();

        var dtos = animals.stream()
                .map(animal -> modelMapper.map(animal, AnimalDTO.class))
                .collect(Collectors.toList());

        Map<AnimalDTO, Double> result = new HashMap<>();

        for (AnimalDTO animalDTO : dtos) {
            for (AnimalMatchingAlgorithm algorithm : algorithms) {
                if (algorithm.applicableTo(animalDTO)) {
                    result.putAll(algorithm.calculateMatching(form, List.of(animalDTO)));
                    break;
                }
            }
        }

        List<Map.Entry<AnimalDTO, Double>> entries = new ArrayList<>(result.entrySet());
        entries.sort(Map.Entry.<AnimalDTO, Double>comparingByValue().reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), entries.size());

        return new PageImpl<>(entries.subList(start, end), pageable, entries.size());
    }
}
