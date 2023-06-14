package com.example.rescuehubproject.setup;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseDataInitializer implements DataInitializer {


    private static final String[] animalSpecies = {"Kot", "Koń", "Pies"};
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnimalSpeciesRepository animalSpeciesRepository;
    @Autowired
    private  PasswordEncoder encoder;

    @Override
    public void initializeData() {
        User user1 = new User();
        user1.setName("admin");
        user1.setLastname("admin");
        user1.setEmail("admin.admin@example.com");
        user1.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        user1.addRole(Role.ROLE_ADMINISTRATOR);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("user");
        user2.setLastname("user");
        user2.setEmail("adopter1@example.com");
        user2.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        user2.addRole(Role.ROLE_ADOPTER);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("user");
        user3.setLastname("user");
        user3.setEmail("adopter2@example.com");
        user3.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        user3.addRole(Role.ROLE_ADOPTER);
        userRepository.save(user3);

        User user4 = new User();
        user4.setName("user");
        user4.setLastname("user");
        user4.setEmail("adopter3@example.com");
        user4.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        user4.addRole(Role.ROLE_ADOPTER);
        userRepository.save(user4);



        initializeAnimalsSpecies(animalSpecies);



    }

    private void initializeAnimalsSpecies(String[] animalSpecies) {
        for (String name : animalSpecies) {
               AnimalSpecies animalSpecies1 = new AnimalSpecies();
                animalSpecies1.setSpeciesName(name);
                animalSpeciesRepository.save(animalSpecies1);
        }
    }
}
