package com.example.tickets.dto;

import com.example.tickets.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProjectRequest {
    @NotBlank
    @Size(min = 1, max = 120)
    private String name;
    private String description;
    private ProjectStatus status;
}
