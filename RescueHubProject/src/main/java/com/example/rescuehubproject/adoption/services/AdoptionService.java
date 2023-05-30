package com.example.rescuehubproject.adoption.services;

import com.example.rescuehubproject.adoption.dto.AdoptionDTO;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.example.rescuehubproject.adoption.repositories.AdoptionRepository;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.animals.entity.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;

    @Autowired
    public AdoptionService(AdoptionRepository adoptionRepository) {
        this.adoptionRepository = adoptionRepository;
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
}
