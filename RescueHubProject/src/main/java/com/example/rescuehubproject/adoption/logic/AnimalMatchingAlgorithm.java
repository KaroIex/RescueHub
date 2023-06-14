package com.example.rescuehubproject.adoption.logic;

import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;

import java.util.List;
import java.util.Map;

public interface AnimalMatchingAlgorithm {

    Map<AnimalDTO, Double> calculateMatching(AdoptionFormDTO form, List<AnimalDTO> animals);
}