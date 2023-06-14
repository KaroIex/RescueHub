package com.example.rescuehubproject.adopters.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.exceptions.AnimalAlreadyAdoptedException;
import com.example.rescuehubproject.adopters.exceptions.AnimalNotFoundException;
import com.example.rescuehubproject.adopters.exceptions.UserIsNotAnAdopter;
import com.example.rescuehubproject.adopters.exceptions.UserNotFoundException;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.example.rescuehubproject.adoption.logic.AnimalMatchingAlgorithm;
import com.example.rescuehubproject.adoption.logic.PetMatchingAlgorithm;
import com.example.rescuehubproject.adoption.repositories.AdoptionRepository;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.security.UserDetailsImpl;
import jakarta.persistence.criteria.Join;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AdopterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<GetAdopterDTO> findAll(Pageable pageable, String filter) {
        Specification<User> spec = createSpecification(filter);
        Page<User> users = userRepository.findAll(spec.and(hasRole(Role.ROLE_ADOPTER)), pageable);
        return users.map(user -> modelMapper.map(user, GetAdopterDTO.class));
    }

    private Specification<User> createSpecification(String filter) {
        return (root, query, cb) -> {
            if (filter == null || filter.isEmpty()) {
                return cb.conjunction(); // return 'AND TRUE'
            } else {
                return cb.like(cb.lower(root.get("email")), "%" + filter.toLowerCase() + "%");
            }
        };
    }

    private Specification<User> hasRole(Role role) {
        return (root, query, cb) -> {
            Join<User, Role> rolesJoin = root.join("roles");
            return cb.equal(rolesJoin, role);
        };
    }

    public GetAdopterByIdDTO findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRoles().contains(Role.ROLE_ADOPTER)) {
                return modelMapper.map(user, GetAdopterByIdDTO.class);
            }
        }
        return null;
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

        AnimalMatchingAlgorithm algorithm = new PetMatchingAlgorithm();

        List<Animal> animals = animalRepository.findAll();

        var dtos = animals.stream()
                .map(animal -> modelMapper.map(animal, AnimalDTO.class))
                .collect(Collectors.toList());

        Map<AnimalDTO, Double> result = algorithm.calculateMatching(form, dtos);

        List<Map.Entry<AnimalDTO, Double>> entries = new ArrayList<>(result.entrySet());
        entries.sort(Map.Entry.comparingByValue());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), entries.size());

        return new PageImpl<>(entries.subList(start, end), pageable, entries.size());
    }



//    public Adopter updateAdopter(Long id, UpdateAdopterDTO updateAdopterDTO) {
//        Optional<User> userOptional = adopterRepository.findById(id);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            if (user.getRoles().contains(Role.ROLE_ADOPTER)) {
//                Adopter adopter = (Adopter) user;
//                adopter.setName(updateAdopterDTO.getName());
//                adopter.setLastname(updateAdopterDTO.getLastname());
//                adopter.setEmail(updateAdopterDTO.getEmail());
//                adopter.setPhone(updateAdopterDTO.getPhone());
//                adopterRepository.save(adopter);
//                return adopter;
//            }
//        }
//        return null;
//    }
}