package com.example.tickets.service;

import com.example.tickets.dto.TicketHistoryResponse;
import com.example.tickets.entity.Project;
import com.example.tickets.entity.Ticket;
import com.example.tickets.entity.TicketHistory;
import com.example.tickets.entity.User;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.repository.ProjectRepository;
import com.example.tickets.repository.TicketHistoryRepository;
import com.example.tickets.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketHistoryService {

    private final TicketHistoryRepository repo;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;

    /** Uložení jedné změny */
    public void saveChange(Ticket ticket, User user, String field, String oldValue, String newValue) {
        TicketHistory history = new TicketHistory();
        history.setTicket(ticket);
        history.setChangedBy(user);
        history.setField(field);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setChangedAt(Instant.now());
        repo.save(history);
    }

    /** Načtení historie jednoho ticketu */
    public List<TicketHistoryResponse> getHistory(Long projectId, Long ticketId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        Ticket ticket = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        return repo.findByTicketOrderByChangedAtDesc(ticket)
                .stream()
                .map(h -> new TicketHistoryResponse(
                        h.getField(),
                        h.getOldValue(),
                        h.getNewValue(),
                        h.getChangedAt().toString(),
                        h.getChangedBy().getUsername()
                ))
                .toList();
    }
}
