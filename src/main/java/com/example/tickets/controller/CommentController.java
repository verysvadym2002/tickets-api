package com.example.tickets.controller;

import com.example.tickets.dto.CommentRequest;
import com.example.tickets.dto.CommentResponse;
import com.example.tickets.dto.CommentUpdateRequest;
import com.example.tickets.security.SecurityUtils;
import com.example.tickets.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tickets/{ticketId}/comments")
public class CommentController {

    private final CommentService service;

    @GetMapping
    public List<CommentResponse> getComments(@PathVariable Long ticketId) {
        return service.getComments(ticketId);
    }

    @PostMapping
    public CommentResponse addComment(
            @PathVariable Long ticketId,
            @RequestBody CommentRequest req
    ) {
        String username = SecurityUtils.currentUsername();
        return service.addComment(ticketId, req, username);
    }

    // -----------------------------------------
    //               UPDATE COMMENT
    // -----------------------------------------
    @PutMapping("/{commentId}")
    public CommentResponse updateComment(
            @PathVariable Long ticketId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest req
    ) {
        String username = SecurityUtils.currentUsername();
        return service.updateComment(ticketId, commentId, username, req.getText());
    }

    // -----------------------------------------
    //               DELETE COMMENT
    // -----------------------------------------
    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long ticketId,
            @PathVariable Long commentId
    ) {
        String username = SecurityUtils.currentUsername();
        service.deleteComment(ticketId, commentId, username);
    }
}
