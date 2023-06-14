package com.example.rescuehubproject.adoption.logic;

import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.entity.Animal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

public class PetMatchingAlgorithm implements AnimalMatchingAlgorithm {

    @Override
    public Map<AnimalDTO, Double> calculateMatching(AdoptionFormDTO form, List<AnimalDTO> animals) {
        Map<AnimalDTO, Double> matchingMap = new HashMap<>();

        for (AnimalDTO animal : animals) {
            double matchRate = 0;
            if (form.isHasBigGarden() == animal.isNeedsOutdoorSpace()) {
                matchRate += 0.5;
            }
            if (form.isHasMuchFreeTime() == animal.isNeedsAttention()) {
                matchRate += 0.5;
            }

            matchingMap.put(animal, matchRate);
        }

        return matchingMap;

    }
}
