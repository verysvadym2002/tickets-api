package com.example.tickets.controller;

import com.example.tickets.dto.ProjectRequest;
import com.example.tickets.dto.ProjectResponse;
import com.example.tickets.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService svc;

    public ProjectController(ProjectService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> list() {
        return ResponseEntity.ok(svc.listOwn());
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest req) {
        return ResponseEntity.ok(svc.create(req));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> get(@PathVariable Long projectId) {
        return ResponseEntity.ok(svc.get(projectId));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> put(@PathVariable Long projectId, @Valid @RequestBody ProjectRequest req) {
        return ResponseEntity.ok(svc.update(projectId, req));
    }

    @PatchMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> patch(@PathVariable Long projectId, @RequestBody ProjectRequest req) {
        return ResponseEntity.ok(svc.patch(projectId, req));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> delete(@PathVariable Long projectId) {
        svc.delete(projectId);
        return ResponseEntity.noContent().build();
    }
}
