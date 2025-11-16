package com.example.tickets.repository;

import com.example.tickets.entity.Ticket;
import com.example.tickets.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistory> findByTicketOrderByChangedAtDesc(Ticket ticket);
}
