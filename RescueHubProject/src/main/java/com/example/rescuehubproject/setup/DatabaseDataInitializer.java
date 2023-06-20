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
import java.util.Random;

@Component
public class DatabaseDataInitializer implements DataInitializer {

    private static final int NUMBER_OF_USERS = 10;
    private static final int NUMBER_OF_ADMINS = 2;
    private static final int NUMBER_OF_ADOPTERS = 100;


    private static final String[] animalSpecies = {"Kot", "Koń", "Pies", "Chomik", "Królik", "Świnka morska", "Jeż", "Papuga", "Wąż", "Inne"};
    private static final String[] animalNames = {"Rex", "Bella", "Max", "Lucy", "Charlie", "Molly", "Buddy", "Daisy", "Rocky", "Maggie",
            "Bailey", "Sophie", "Buster", "Sadie", "Cody", "Ginger", "Jake", "Zoe", "Duke", "Chloe", "Jack", "Lola", "Harley", "Abby", "Toby", "Roxy",
            "Tucker", "Gracie", "Bear", "Coco", "Oliver", "Sasha", "Teddy", "Annie", "Riley", "Mia", "Winston", "Lily", "Murphy", "Ruby", "Leo", "Penny",
            "Oscar", "Emma", "Louie", "Samantha", "Bentley", "Sassy", "Zeus", "Sandy", "Henry", "Katie", "Sam", "Nikki", "Harley", "Misty", "Baxter", "Princess",
            "Gus", "Baby", "Loki", "Missy", "Jackson", "Bailey", "Bruno", "Brandy", "Diesel", "Holly", "Jax", "Jasmine", "Bandit", "Shelby", "Milo", "Sugar",
            "Scout", "Dakota", "Shadow", "Maddie", "Beau", "Lady", "Bandit", "Heidi", "Rusty", "Lulu", "Benny", "Lucky", "Dexter", "Phoebe", "Hunter", "Cleo",
            "Moose", "Casey", "Rocco", "Mimi", "Apollo", "Honey", "Copper", "Roxie", "Romeo", "Callie", "Otis", "Angel", "King", "Dixie", "Chance", "Ellie",
            "Rex", "Hannah", "Cash", "Guinness", "Joey", "Macy", "Archie", "Mocha", "Rudy", "Muffin", "Prince", "Jade", "Samson", "Piper", "Blue", "Cinnamon",
            "Simba", "Josie", "Marley", "Hazel", "Tyson", "Savannah", "Elvis", "Cleopatra", "Boomer", "Cali", "Bo", "Delilah", "Luke", "Jasmine", "Chico", "Cassie",
            "Cooper", "Bonnie", "Chase", "Allie", "Bubba", "Sassy", "Brady", "Samantha", "Cody", "Sasha", "Hank", "Sassy", "Moose", "Sassy", "Beau", "Sassy",
            "Diesel", "Sassy", "Bruno", "Sassy", "Dexter", "Sassy", "Prince", "Sassy", "Romeo", "Sassy", "Otis", "Sassy", "King", "Sassy", "Chance", "Sassy",};

    private static final String[] firstNames = {"John", "Jane", "Alex", "Alice", "Charlie", "Charlotte", "Daniel", "Emily", "David", "Emma",
            "George", "Laura", "Leo", "Lucy", "Sam", "Sophia"};
    private static final String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};


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
        initializeUsers(NUMBER_OF_ADMINS, Role.ROLE_ADMINISTRATOR);
        initializeUsers(NUMBER_OF_ADOPTERS, Role.ROLE_ADOPTER);
        initializeUsers(NUMBER_OF_USERS, Role.ROLE_USER);
        initialize();
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

    private void initialize(){
        User user1 = new User();
        user1.setName("user1");
        user1.setLastname("user1");
        user1.setEmail("user1@test.com");
        user1.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        user1.addRole(Role.ROLE_USER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("adopter1");
        user2.setLastname("adopter1");
        user2.setEmail("adopter1@test.com");
        user2.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        Role role = Role.ROLE_ADOPTER;
        user2.addRole(role);
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("admin");
        user3.setLastname("admin");
        user3.setEmail("admin@test.com");
        user3.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
        Role role2 = Role.ROLE_ADMINISTRATOR;
        user3.addRole(role2);
        userRepository.save(user3);


        if (role == Role.ROLE_ADOPTER) {
            Adopter adopter = new Adopter();
            adopter.setUser(user2);
            Random random = new Random();
            adopter.setPhone((String.valueOf(random.nextInt(999999999) + 100000000)));
            adopter.setCity("Test city");
            adopter.setCountry("Test country");
            adopter.setStreet("Test street");
            adopter.setZip("Test zip code");
            adopter.setState("Test state");
            adopterRepository.save(adopter);
            user2.setAdopter(adopter);
        }
    }

    private void initializeUsers(int n, Role role) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            User user = new User();
            user.setName(firstName);
            user.setLastname(lastName);
            if (role == Role.ROLE_ADOPTER) {
                user.setEmail(firstName.toLowerCase() + lastName.toLowerCase() + i + "@adopter.com");
            } else if (role == Role.ROLE_USER) {
                user.setEmail(firstName.toLowerCase() + lastName.toLowerCase() + i + "@user.com");
            } else {
                user.setEmail(firstName.toLowerCase() + lastName.toLowerCase() + i + "@admin.com");
            }
            user.setPassword(encoder.encode("!QAZXSW@#EDCVFR$"));
            user.addRole(role);
            userRepository.save(user);

            if (role == Role.ROLE_ADOPTER) {
                Adopter adopter = new Adopter();
                adopter.setUser(user);
                adopter.setPhone((String.valueOf(random.nextInt(999999999) + 100000000)));
                adopter.setCity("Test city");
                adopter.setCountry("Test country");
                adopter.setStreet("Test street");
                adopter.setZip("Test zip code");
                adopter.setState("Test state");
                adopterRepository.save(adopter);
                user.setAdopter(adopter);
            }

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

    private void initializeAdoption() {
        // Random random = new Random();
        LocalDate date = LocalDate.now();
        Adoption adoption = new Adoption();
        Animal animal = animalRepository.findById((long) 2).orElseThrow();
        Adopter adopter = adopterRepository.findById((long) 2).orElseThrow();

        adoption.setAdoptionDate(date);
        adoption.setStatus(Status.NEW);
        if (animal != null && adopter != null) {
            adoption.setAdopter(adopter);
            adoption.setAnimal(animal);
            adoptionRepository.save(adoption);
        }
    }


}
