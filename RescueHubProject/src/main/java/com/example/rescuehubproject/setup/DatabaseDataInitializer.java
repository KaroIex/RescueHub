package com.example.rescuehubproject.setup;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.repositories.UserRepository;
import com.example.rescuehubproject.accounts.util.Role;
import com.example.rescuehubproject.adopters.entities.Adopter;
import com.example.rescuehubproject.adopters.repositories.AdopterRepository;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.example.rescuehubproject.adoption.repositories.AdoptionRepository;
import com.example.rescuehubproject.adoption.util.Status;
import com.example.rescuehubproject.animals.entity.Animal;
import com.example.rescuehubproject.animals.entity.AnimalSpecies;
import com.example.rescuehubproject.animals.repositories.AnimalRepository;
import com.example.rescuehubproject.animals.repositories.AnimalSpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Component
public class DatabaseDataInitializer implements DataInitializer {


    private static final String[] animalSpecies = {"Kot", "Koń", "Pies", "Chomik", "Królik", "Świnka morska", "Jeż", "Papuga", "Wąż", "Inne"};
    private static final String[] animalNames = {"Rex", "Bella", "Max", "Lucy", "Charlie", "Molly", "Buddy", "Daisy", "Rocky", "Maggie",
    "Bailey", "Sophie", "Buster", "Sadie", "Cody", "Ginger", "Jake", "Zoe", "Duke", "Chloe", "Jack", "Lola", "Harley", "Abby", "Toby", "Roxy",
    "Tucker", "Gracie", "Bear", "Coco", "Oliver", "Sasha", "Teddy", "Annie", "Riley", "Mia", "Winston", "Lily", "Murphy", "Ruby", "Leo", "Penny",
    "Oscar", "Emma", "Louie", "Samantha", "Bentley", "Sassy", "Zeus", "Sandy", "Henry", "Katie", "Sam", "Nikki", "Harley", "Misty", "Baxter", "Princess",
    "Gus", "Baby", "Loki", "Missy", "Jackson", "Bailey", "Bruno", "Brandy", "Diesel", "Holly", "Jax", "Jasmine", "Bandit", "Shelby", "Milo", "Sugar"};
    private static final String[] admins = {"admin"};
    private static final String[] adopters = {"adopter1", "adopter2", "adopter3", "adopter4"};
    private static final String[] users = {"user1", "user2", "user3", "user4"};
    @Autowired
    AnimalRepository animalRepository;
    @Autowired
    private AnimalSpeciesRepository animalSpeciesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdopterRepository adopterRepository;
    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void initializeData() {
        initializeUsers(admins, Role.ROLE_ADMINISTRATOR);
        initializeUsers(adopters, Role.ROLE_ADOPTER);
        initializeUsers(users, Role.ROLE_USER);
        initializeAnimalsSpecies(animalSpecies);
        initializeAnimals(animalNames);
        initializeAdoption();

    }

    private void initializeAnimalsSpecies(String[] animalSpecies) {
        for (String name : animalSpecies) {
            AnimalSpecies animalSpecies1 = new AnimalSpecies();
            animalSpecies1.setSpeciesName(name);
            animalSpeciesRepository.save(animalSpecies1);
        }
    }

    private void initializeUsers(String[] admins, Role role) {
        for (String name : admins) {
            User user = new User();
            user.setName(name);
            user.setLastname(name);
            user.setEmail(name + "@test.com");
            user.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
            user.addRole(role);
            userRepository.save(user);
        }
    }

    private void initializeAnimals(String[] animalNames) {
        Random random = new Random();

        for (String name : animalNames) {
            Animal animal = new Animal();
            animal.setName(name);
            animal.setAge(random.nextInt(15) + 1);
            animal.setDescription("This is a " + name);
            animal.setNeedsAttention(random.nextBoolean());
            animal.setSocialAnimal(random.nextBoolean());
            animal.setNeedsOutdoorSpace(random.nextBoolean());
            animal.setGoodWithChildren(random.nextBoolean());
            AnimalSpecies species = animalSpeciesRepository.findById((long) (random.nextInt(10) + 1)).orElse(null);

            if (species != null) {
                animal.setAnimalSpecies(species);
                animalRepository.save(animal);
            }
        }
    }

    private void initializeAdoption (){
       // Random random = new Random();
        LocalDate date = LocalDate.now();
        Adoption adoption = new Adoption();
        Animal animal = animalRepository.findById((long) 2).orElseThrow();
        Adopter adopter = adopterRepository.findById((long) 2).orElseThrow();

        adoption.setAdoptionDate(date);
        adoption.setStatus(Status.NEW);
        if(animal != null && adopter != null){
            adoption.setAdopter(adopter);
            adoption.setAnimal(animal);
            adoptionRepository.save(adoption);
        }
    }


}
