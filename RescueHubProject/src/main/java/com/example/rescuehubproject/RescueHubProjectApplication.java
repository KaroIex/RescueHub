package com.example.rescuehubproject;

import com.example.rescuehubproject.setup.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RescueHubProjectApplication {

    @Autowired
    private DataInitializer dataInitializer;

    public static void main(String[] args) {
        SpringApplication.run(RescueHubProjectApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {

            //dataInitializer.initializeData();

        };
    }

}
