package com.example.rescuehubproject.accounts.repositories;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.accounts.util.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // JpaRepository used to work with DB

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsUserByEmailIgnoreCase(String email);

    Set<Role> findRolesById(Long id);

    List<User> findAll();


    Page<User> findAll(Specification<User> and, Pageable pageable);
}