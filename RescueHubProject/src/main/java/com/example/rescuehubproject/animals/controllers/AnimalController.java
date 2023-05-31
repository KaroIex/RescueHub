package com.example.rescuehubproject.animals.controllers;

import com.example.rescuehubproject.animals.dto.AnimalDTO;
import com.example.rescuehubproject.animals.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/api/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping
    public List<AnimalDTO> findAll(){
        return animalService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> findById(@PathVariable Long id){
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        return animalDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AnimalDTO save(@RequestBody AnimalDTO animalDTO){
        return animalService.save(animalDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> update(@PathVariable Long id, @RequestBody AnimalDTO updatedAnimalDTO){
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        if(animalDTO.isPresent()) {
            AnimalDTO updatedAnimal = animalService.save(updatedAnimalDTO);
            return ResponseEntity.ok(updatedAnimal);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        Optional<AnimalDTO> animalDTO = animalService.findById(id);
        if(animalDTO.isPresent()){
            animalService.deleteById(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
