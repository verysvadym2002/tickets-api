package com.example.tickets.dto;

import com.example.tickets.entity.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private String ownerUsername;
}
