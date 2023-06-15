package com.example.rescuehubproject.adoption.services;

import com.example.rescuehubproject.adoption.dto.AdoptionFormDTO;
import com.example.rescuehubproject.animals.dto.AnimalDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PetMatchingAlgorithm implements AnimalMatchingAlgorithm {

    @Override
    public Map<AnimalDTO, Double> calculateMatching(AdoptionFormDTO form, List<AnimalDTO> animals) {
        Map<AnimalDTO, Double> matchingMap = new HashMap<>();

        for (AnimalDTO animal : animals) {
            double matchRate = 0;
            if (form.getPreferredSpecies().equals(animal.getAnimalSpecies())) {
                matchRate += 0.3;
            }
            if (form.getPreferredAge() >= animal.getAge() - 2 && form.getPreferredAge() <= animal.getAge() + 2) {
                matchRate += 0.2;
            }
            if (form.isHasChildren() == animal.isGoodWithChildren()) {
                matchRate += 0.2;
            }
            if (form.isHasBigGarden() == animal.isNeedsOutdoorSpace()) {
                matchRate += 0.1;
            }
            if (form.isHasMuchFreeTime() == animal.isNeedsAttention()) {
                matchRate += 0.1;
            }
            matchRate = Math.round(matchRate * 100.0) / 100.0;
            matchingMap.put(animal, matchRate);
        }
        return matchingMap;
    }

    @Override
    public boolean applicableTo(AnimalDTO animal) {
        return !animal.isNeedsOutdoorSpace();
    }
}
