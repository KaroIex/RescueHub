package com.example.rescuehubproject.accounts.repositories;

import com.example.rescuehubproject.accounts.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAll();

}
