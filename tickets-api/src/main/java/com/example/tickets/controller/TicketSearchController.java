package com.example.tickets.controller;

import com.example.tickets.document.TicketDocument;
import com.example.tickets.service.TicketSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class TicketSearchController {

    private final TicketSearchService service;

    /**
     * Fulltext vyhledávání (title + description)
     * GET /api/search/fulltext?q=...
     */
    @GetMapping("/fulltext")
    public List<TicketDocument> fulltext(@RequestParam String q) {
        return service.fulltextSearch(q);
    }

    /**
     * Najdi tikety podle projektu.
     * GET /api/search/project/1
     */
    @GetMapping("/project/{id}")
    public List<TicketDocument> byProject(@PathVariable Long id) {
        return service.findByProject(id);
    }

    /**
     * Komplexní vyhledávání:
     * GET /api/search?q=bug&projectId=1&state=OPEN&priority=HIGH&assigneeId=5
     */
    @GetMapping
    public List<TicketDocument> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String type
    ) {
        return service.search(q, projectId, state, priority, assigneeId, type);
    }
}

