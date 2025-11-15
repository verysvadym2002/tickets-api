package com.example.tickets.repository;

import com.example.tickets.entity.Attachment;
import com.example.tickets.entity.Project;
import com.example.tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByTicket(Ticket ticket);
    List<Attachment> findByProject(Project project);
}
