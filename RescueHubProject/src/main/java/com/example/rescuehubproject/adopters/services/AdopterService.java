package com.example.rescuehubproject.adopters.services;

import com.example.rescuehubproject.adopters.dto.AdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdopterService {

    private final AdopterRepository adopterRepository;

    @Autowired
    public AdopterService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    public List<AdopterDTO> findAll() {
        List<Adopter> adopters = adopterRepository.findAll();
        return adopters.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AdopterDTO> findById(Long id) {
        Optional<Adopter> adopter = adopterRepository.findById(id);
        return adopter.map(this::convertToDTO);
    }

    public AdopterDTO save(AdopterDTO adopterDTO) {
        Adopter adopter = convertToEntity(adopterDTO);
        Adopter savedAdopter = adopterRepository.save(adopter);
        return convertToDTO(savedAdopter);
    }

    public void deleteById(Long id) {
        adopterRepository.deleteById(id);
    }

    private AdopterDTO convertToDTO(Adopter adopter) {
        AdopterDTO adopterDTO = new AdopterDTO();
        BeanUtils.copyProperties(adopter, adopterDTO);
        return adopterDTO;
    }

    private Adopter convertToEntity(AdopterDTO adopterDTO) {
        Adopter adopter = new Adopter();
        BeanUtils.copyProperties(adopterDTO, adopter);
        return adopter;
    }
}