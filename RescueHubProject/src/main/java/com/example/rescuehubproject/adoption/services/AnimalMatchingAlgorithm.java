package com.example.rescuehubproject.adoption.services;

import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface AnimalMatchingAlgorithm {

    Map<AnimalDTO, Double> calculateMatching(AdoptionFormDTO form, List<AnimalDTO> animals);
    boolean applicableTo(AnimalDTO animal);
}