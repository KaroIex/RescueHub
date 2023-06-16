package com.example.rescuehubproject.adopters.services;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.dto.GetAdopterByIdDTO;
import com.example.rescuehubproject.adopters.dto.GetAdopterDTO;
import com.example.rescuehubproject.adopters.dto.PutAdopterDTO;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.exceptions.UserIsNotAnAdopter;
import com.example.rescuehubproject.adopters.exceptions.UserNotFoundException;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.security.UserDetailsImpl;
import jakarta.persistence.criteria.Join;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdopterService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdopterRepository adopterRepository;

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

    public PutAdopterDTO updateAdopter(Authentication authentication, PutAdopterDTO adopterDto) {

        String userId = null;

        if (authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            userId = userDetails.getId();
        } else {
            throw new UserNotFoundException();
        }

        Optional<User> optionalUser = userRepository.findById(Long.parseLong(userId));
        if (!optionalUser.isPresent()) throw new UserNotFoundException();
        User user = optionalUser.get();
        Adopter adopter = user.getAdopter();
        if (adopter == null) throw new UserIsNotAnAdopter();
        adopter.setPhone(adopterDto.getPhone());
        adopter.setStreet(adopterDto.getStreet());
        adopter.setCity(adopterDto.getCity());
        adopter.setState(adopterDto.getState());
        adopter.setZip(adopterDto.getZip());
        adopter.setCountry(adopterDto.getCountry());
        adopterRepository.save(adopter);
        return adopterDto;

    }

}