package com.example.tickets.service;

import com.example.tickets.dto.TicketRequest;
import com.example.tickets.dto.TicketResponse;
import com.example.tickets.entity.Project;
import com.example.tickets.entity.Ticket;
import com.example.tickets.entity.User;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.repository.ProjectRepository;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.repository.UserRepository;
import com.example.tickets.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TicketHistoryService historyService;

    public TicketService(TicketRepository ticketRepository, ProjectRepository projectRepository, UserRepository userRepository,
                         TicketHistoryService historyService) {
        this.ticketRepository = ticketRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.historyService = historyService;
    }

    private User currentUser() {
        String username = SecurityUtils.currentUsername();
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    private Project findProjectOwned(Long projectId) {
        User u = currentUser();
        return projectRepository.findByIdAndOwner(projectId, u)
                .orElseThrow(() -> new NotFoundException("Project not found or not accessible"));
    }

    public TicketResponse create(Long projectId, TicketRequest req) {
        Project project = findProjectOwned(projectId);
        Ticket t = new Ticket();
        t.setTitle(req.getTitle());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getState() != null) t.setState(req.getState());
        t.setProject(project);

        if (req.getAssigneeId() != null) {
            User assignee = userRepository.findById(req.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
            t.setAssignee(assignee);
        }

        Ticket saved = ticketRepository.save(t);
        return toDto(saved);
    }

    public List<TicketResponse> list(Long projectId) {
        Project project = findProjectOwned(projectId);
        return ticketRepository.findAllByProject(project).stream().map(this::toDto).collect(Collectors.toList());
    }

    public TicketResponse get(Long projectId, Long ticketId) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));
        return toDto(t);
    }

    public TicketResponse update(Long projectId, Long ticketId, TicketRequest req) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));
        t.setTitle(req.getTitle());
        if (req.getType() != null) t.setType(req.getType());
        if (req.getPriority() != null) t.setPriority(req.getPriority());
        if (req.getState() != null) t.setState(req.getState());

        if (req.getAssigneeId() != null) {
            User assignee = userRepository.findById(req.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
            t.setAssignee(assignee);
        }

        return toDto(ticketRepository.save(t));
    }

    public TicketResponse patch(Long projectId, Long ticketId, TicketRequest req) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));

        User cu = currentUser(); // zlepší čitelnost

        // TITLE
        if (req.getTitle() != null && !req.getTitle().equals(t.getTitle())) {
            historyService.saveChange(t, cu, "title", t.getTitle(), req.getTitle());
            t.setTitle(req.getTitle());
        }

        // TYPE
        if (req.getType() != null && req.getType() != t.getType()) {
            historyService.saveChange(t, cu, "type",
                    t.getType() == null ? "none" : t.getType().name(),
                    req.getType().name());
            t.setType(req.getType());
        }

        // PRIORITY
        if (req.getPriority() != null && req.getPriority() != t.getPriority()) {
            historyService.saveChange(t, cu, "priority",
                    t.getPriority() == null ? "none" : t.getPriority().name(),
                    req.getPriority().name());
            t.setPriority(req.getPriority());
        }

        // STATE
        if (req.getState() != null && req.getState() != t.getState()) {
            historyService.saveChange(t, cu, "state",
                    t.getState().name(), req.getState().name());
            t.setState(req.getState());
        }

        // ASSIGNEE
        if (req.getAssigneeId() != null) {
            User newAssignee = userRepository.findById(req.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));

            Long oldId = t.getAssignee() != null ? t.getAssignee().getId() : null;

            if (!req.getAssigneeId().equals(oldId)) {
                historyService.saveChange(t, cu, "assignee",
                        t.getAssignee() == null ? "none" : t.getAssignee().getUsername(),
                        newAssignee.getUsername());
                t.setAssignee(newAssignee);
            }
        }

        ticketRepository.save(t);
        return toDto(t);
    }

    public void delete(Long projectId, Long ticketId) {
        Project project = findProjectOwned(projectId);
        Ticket t = ticketRepository.findByIdAndProject(ticketId, project)
                .orElseThrow(() -> new NotFoundException("Ticket not found or not accessible"));
        ticketRepository.delete(t);
    }

    private TicketResponse toDto(Ticket t) {
        return new TicketResponse(
                t.getId(),
                t.getTitle(),
                t.getType(),
                t.getPriority(),
                t.getState(),
                t.getProject().getId(),
                t.getAssignee() != null ? t.getAssignee().getId() : null,
                t.getAssignee() != null ? t.getAssignee().getUsername() : null
        );
    }
}
