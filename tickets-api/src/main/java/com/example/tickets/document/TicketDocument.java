package com.example.tickets.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import java.time.Instant;

@Document(indexName = "tickets")
public class TicketDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long ticketId;

    @Field(type = FieldType.Keyword)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String state;

    @Field(type = FieldType.Keyword)
    private String priority;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Long)
    private Long projectId;

    @Field(type = FieldType.Long)
    private Long assigneeId;

    @Field(type = FieldType.Keyword)
    private String assigneeUsername;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant updatedAt;

    public TicketDocument() {}

    public TicketDocument(String id, Long ticketId, String title, String description,
                          String state, String priority, String type,
                          Long projectId, Long assigneeId, String assigneeUsername,
                          Instant updatedAt) {
        this.id = id;
        this.ticketId = ticketId;
        this.title = title;
        this.description = description;
        this.state = state;
        this.priority = priority;
        this.type = type;
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assigneeUsername = assigneeUsername;
        this.updatedAt = updatedAt;
    }

    // GETTERS + SETTERS

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }

    public String getAssigneeUsername() { return assigneeUsername; }
    public void setAssigneeUsername(String assigneeUsername) { this.assigneeUsername = assigneeUsername; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
