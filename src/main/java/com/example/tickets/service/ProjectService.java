package com.example.tickets.service;

import com.example.tickets.dto.ProjectRequest;
import com.example.tickets.dto.ProjectResponse;
import com.example.tickets.entity.Project;
import com.example.tickets.entity.User;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.repository.ProjectRepository;
import com.example.tickets.repository.UserRepository;
import com.example.tickets.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    private User currentUser() {
        String username = SecurityUtils.currentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    public ProjectResponse create(ProjectRequest req) {
        User owner = currentUser();
        Project p = new Project();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        if (req.getStatus() != null) p.setStatus(req.getStatus());
        p.setOwner(owner);
        Project saved = projectRepository.save(p);
        return toDto(saved);
    }

    public List<ProjectResponse> listOwn() {
        User owner = currentUser();
        return projectRepository.findAllByOwner(owner)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ProjectResponse get(Long projectId) {
        Project p = findAndCheckOwner(projectId);
        return toDto(p);
    }

    public ProjectResponse update(Long projectId, ProjectRequest req) {
        Project p = findAndCheckOwner(projectId);
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        if (req.getStatus() != null) p.setStatus(req.getStatus());
        Project saved = projectRepository.save(p);
        return toDto(saved);
    }

    public ProjectResponse patch(Long projectId, ProjectRequest req) {
        Project p = findAndCheckOwner(projectId);
        if (req.getName() != null) p.setName(req.getName());
        if (req.getDescription() != null) p.setDescription(req.getDescription());
        if (req.getStatus() != null) p.setStatus(req.getStatus());
        return toDto(projectRepository.save(p));
    }

    public void delete(Long projectId) {
        Project p = findAndCheckOwner(projectId);
        projectRepository.delete(p);
    }

    private Project findAndCheckOwner(Long id) {
        User owner = currentUser();
        return projectRepository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new NotFoundException("Project not found or not accessible"));
    }

    private ProjectResponse toDto(Project p) {
        return new ProjectResponse(p.getId(), p.getName(), p.getDescription(), p.getStatus(), p.getOwner().getUsername());
    }
}
