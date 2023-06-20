package com.example.rescuehubproject.accounts.services;

import com.example.rescuehubproject.accounts.entity.Log;
import com.example.rescuehubproject.accounts.repositories.LogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    LogRepository logRepository;


    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void saveLog(Log log) {
        logRepository.save(log);
    }

    public ResponseEntity<?> getAllLogs() {
        return new ResponseEntity<>(logRepository.findAll(), org.springframework.http.HttpStatus.OK);
    }


}
