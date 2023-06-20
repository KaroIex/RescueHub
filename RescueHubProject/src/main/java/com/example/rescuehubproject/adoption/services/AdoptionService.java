package com.example.rescuehubproject.adoption.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.exceptions.*;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.adoption.dto.MyAdoptionDTO;
import com.example.rescuehubproject.adoption.dto.AdoptionDTO;
import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.adoption.dto.AdoptionStatusDTO;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.example.rescuehubproject.adoption.repositories.AdoptionRepository;
import com.example.rescuehubproject.adoption.util.Status;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.dto.PaginatedResponse;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.security.UserDetailsImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final AdopterRepository adopterRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final List<AnimalMatchingAlgorithm> algorithms;
    private final ModelMapper modelMapper;

    @Autowired
    public AdoptionService(AdoptionRepository adoptionRepository, UserRepository userRepository, AnimalRepository animalRepository, List<AnimalMatchingAlgorithm> algorithms, ModelMapper modelMapper, AdopterRepository adopterRepository) {
        this.adoptionRepository = adoptionRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.algorithms = algorithms;
        this.modelMapper = modelMapper;
        this.adopterRepository = adopterRepository;
    }


    public List<AdoptionDTO> getAllAdoptions() {
        return adoptionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AdoptionDTO> getAdoptionById(Long id) {
        return adoptionRepository.findById(id)
                .map(this::convertToDTO);
    }

    public AdoptionDTO createAdoption(AdoptionDTO adoptionDTO) {
        Adoption adoption = convertToEntity(adoptionDTO);
        adoption.setStatus(Status.NEW);
        Adoption savedAdoption = adoptionRepository.save(adoption);
        return convertToDTO(savedAdoption);
    }

    public void removeAdoption(Long id) {
        adoptionRepository.deleteById(id);
    }

    private AdoptionDTO convertToDTO(Adoption adoption) {
        AdoptionDTO adoptionDTO = modelMapper.map(adoption, AdoptionDTO.class);
        return adoptionDTO;
    }

    private Adoption convertToEntity(AdoptionDTO adoptionDTO) {
        Adoption adoption = modelMapper.map(adoptionDTO, Adoption.class);
        return adoption;
    }

    public Adoption adopt(Authentication authentication, Long animalId) {


        String userId = null;

        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            userId = userDetails.getId();
        } else {
            throw new UserNotFoundException();
        }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        Optional<Animal> animalOptional = animalRepository.findById(animalId);



        if (!userOptional.isPresent()) throw new UserNotFoundException();
        if (!animalOptional.isPresent()) throw new AnimalNotFoundException();

        User user = userOptional.get();
        Animal animal = animalOptional.get();


        if (!user.getRoles().contains(Role.ROLE_ADOPTER)) throw new UserIsNotAnAdopter();
        if (user.getAdopter() == null) throw new EmptyAdopterDetailsExceptions();
        if (animal.getAdoptions().size() > 0) throw new AnimalAlreadyAdoptedException();

        Adopter adopter = user.getAdopter();
        Adoption adoption = new Adoption();

        adoption.setAdoptionDate(LocalDate.now());
        adoption.setStatus(Status.NEW);
        adoption.setAdopter(adopter);
        adoption.setAnimal(animal);
        adoptionRepository.save(adoption);

        animal.addAdoption(adoption);
        animalRepository.save(animal);

        return adoption;
    }


    public Page<Map.Entry<AnimalDTO, Double>> matchAdopterToAnimal(AdoptionFormDTO form, Pageable pageable) {

        List<Animal> animals = animalRepository.findAll();

        var dtos = animals.stream().map(animal -> modelMapper.map(animal, AnimalDTO.class)).collect(Collectors.toList());

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

    public AdoptionStatusDTO updateStatus(Long id, AdoptionStatusDTO updatedAdoptionStatusDTO) throws NoSuchFieldException {
        Optional<Adoption> exsistingAdoptionOptional = adoptionRepository.findById(id);

        if (exsistingAdoptionOptional.isPresent()) {
            Adoption exsistingAdoption = exsistingAdoptionOptional.get();
            LocalDate date = LocalDate.now();
            exsistingAdoption.setAdoptionDate(date);

            List<Adopter> adopters = adopterRepository.findAll();
            for (Adopter adopter : adopters) {
                System.out.println(adopter.getUser().getName());
                System.out.println(updatedAdoptionStatusDTO.getAdopterName());
                if (Objects.equals(adopter.getUser().getName(), updatedAdoptionStatusDTO.getAdopterName())) {
                    List<Animal> animals = animalRepository.findAll();
                    for (Animal animal : animals) {
                        if (Objects.equals(animal.getName(), updatedAdoptionStatusDTO.getAnimalName())) {
                            if (Objects.equals(exsistingAdoption.getStatus(), Status.NEW) && (Objects.equals(updatedAdoptionStatusDTO.getStatus(), "EXPECT"))) {
                                exsistingAdoption.setStatus(Status.getStatus(updatedAdoptionStatusDTO.getStatus()));
                                Adoption updatedAdoption = adoptionRepository.save(exsistingAdoption);
                                return convertToDTOStatus(updatedAdoption);
                            } else if (Objects.equals(exsistingAdoption.getStatus(), Status.EXPECT) && (Objects.equals(updatedAdoptionStatusDTO.getStatus(), "ACCEPT"))) {
                                exsistingAdoption.setStatus(Status.getStatus(updatedAdoptionStatusDTO.getStatus()));
                                Adoption updatedAdoption = adoptionRepository.save(exsistingAdoption);
                                return convertToDTOStatus(updatedAdoption);
                            } else if (Objects.equals(exsistingAdoption.getStatus(), Status.getStatus(updatedAdoptionStatusDTO.getStatus()))) {
                                throw new NoSuchFieldException("The status has already been set");
                            } else if ((Objects.equals(exsistingAdoption.getStatus(), Status.EXPECT) && (Objects.equals(updatedAdoptionStatusDTO.getStatus(), "NEW"))) || (Objects.equals(exsistingAdoption.getStatus(), Status.NEW) && (Objects.equals(updatedAdoptionStatusDTO.getStatus(), "ACCEPT"))) || (Objects.equals(exsistingAdoption.getStatus(), Status.ACCEPT)))
                                throw new NoSuchFieldException("statuses can be changed in this order : NEW -> EXPECT -> ACCEPT");
                            throw new NoSuchFieldException("Error status");
                        }
                    }
                    throw new NoSuchFieldException("Error animal");
                }
            }
            throw new NoSuchFieldException("Error adopter");

        }
        throw new NoSuchFieldException("Error");
    }

    private AdoptionStatusDTO convertToDTOStatus(Adoption updatedAdoption) throws NoSuchFieldException {
        AdoptionStatusDTO adoptionStatusDTO = new AdoptionStatusDTO(updatedAdoption.getAdopter().getUser().getName(), updatedAdoption.getAnimal().getName());
        switch (updatedAdoption.getStatus()) {
            case NEW:
                adoptionStatusDTO.setStatus("NEW");
                return adoptionStatusDTO;
            case ACCEPT:
                adoptionStatusDTO.setStatus("ACCEPT");
                return adoptionStatusDTO;
            case EXPECT:
                adoptionStatusDTO.setStatus("EXPECT");
                return adoptionStatusDTO;
            default:
                throw new NoSuchFieldException("Error status");
        }
    }


    public PaginatedResponse<MyAdoptionDTO> getAllAdoptionsByUserPagination(Authentication authentication, int currentPage, int size) {
        String userId = null;

        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            userId = userDetails.getId();
        } else {
            throw new UserNotFoundException();
        }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));

        if (!userOptional.isPresent()) throw new UserNotFoundException();

        User user = userOptional.get();

        if (!user.getRoles().contains(Role.ROLE_ADOPTER)) throw new UserIsNotAnAdopter();

        Adopter adopter = user.getAdopter();

        Pageable pageable = PageRequest.of(currentPage, size);

        Page<Adoption> adoptionPage = adoptionRepository.findAllByAdopter(adopter, pageable);

        List<MyAdoptionDTO> content =
                adoptionPage.getContent().stream().map(adoption -> modelMapper.map(adoption, MyAdoptionDTO.class)).collect(Collectors.toList());

        PaginatedResponse<MyAdoptionDTO> paginatedResponse = new PaginatedResponse<MyAdoptionDTO>();
        paginatedResponse.setCurrentPage(adoptionPage.getNumber());
        paginatedResponse.setSize(adoptionPage.getSize());
        paginatedResponse.setTotalElements(adoptionPage.getTotalElements());
        paginatedResponse.setContent(content);
        return paginatedResponse;
    }
}
