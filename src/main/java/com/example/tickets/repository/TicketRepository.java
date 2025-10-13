package com.example.tickets.repository;

import com.example.tickets.entity.Ticket;
import com.example.tickets.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByProject(Project project);
    Optional<Ticket> findByIdAndProject(Long id, Project project);
}
