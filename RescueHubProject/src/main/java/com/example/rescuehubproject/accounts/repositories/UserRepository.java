package com.example.rescuehubproject.accounts.repositories;

import com.example.rescuehubproject.accounts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // JpaRepository used to work with DB

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);

}