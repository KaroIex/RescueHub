package com.example.rescuehubproject.adopters.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.UpdateAdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<GetAdopterDTO> findAll(Pageable pageable, String filter) {
        // Implement your filtering logic and retrieve adopters accordingly.
        Page<User> users = adopterRepository.findAll(pageable);
        return users.map(user -> modelMapper.map(user, GetAdopterDTO.class));
    }

    public GetAdopterByIdDTO findById(Long id) {
        Optional<User> userOptional = adopterRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRoles().contains(Role.ROLE_ADOPTER)) {
                return modelMapper.map(user, GetAdopterByIdDTO.class);
            }
        }
        return null;
    }

    public Adopter updateAdopter(Long id, UpdateAdopterDTO updateAdopterDTO) {
        Optional<User> userOptional = adopterRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getRoles().contains(Role.ROLE_ADOPTER)) {
                Adopter adopter = (Adopter) user;
                adopter.setName(updateAdopterDTO.getName());
                adopter.setLastname(updateAdopterDTO.getLastname());
                adopter.setEmail(updateAdopterDTO.getEmail());
                adopter.setPhone(updateAdopterDTO.getPhone());
                adopterRepository.save(adopter);
                return adopter;
            }
        }
        return null;
    }
}