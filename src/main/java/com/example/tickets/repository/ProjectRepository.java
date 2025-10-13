package com.example.tickets.repository;

import com.example.tickets.entity.Project;
import com.example.tickets.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwner(User owner);
    Optional<Project> findByIdAndOwner(Long id, User owner);
}
