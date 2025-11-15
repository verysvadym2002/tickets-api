package com.example.tickets.service;

import com.example.tickets.dto.CommentRequest;
import com.example.tickets.dto.CommentResponse;
import com.example.tickets.dto.CommentUpdateRequest;
import com.example.tickets.entity.Comment;
import com.example.tickets.entity.Ticket;
import com.example.tickets.entity.User;
import com.example.tickets.exception.NotFoundException;
import com.example.tickets.repository.CommentRepository;
import com.example.tickets.repository.TicketRepository;
import com.example.tickets.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public List<CommentResponse> getComments(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        return commentRepository.findByTicket(ticket)
                .stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getUsername(),
                        c.getCreatedAt().toString()
                ))
                .toList();
    }

    public CommentResponse addComment(Long ticketId, CommentRequest req, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setText(req.getText());
        comment.setCreatedAt(Instant.now());
        comment.setTicket(ticket);
        comment.setAuthor(author);

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                saved.getText(),
                saved.getAuthor().getUsername(),
                saved.getCreatedAt().toString()
        );
    }

    // -----------------------------------------
    //             UPDATE COMMENT
    // -----------------------------------------
    public CommentResponse updateComment(Long ticketId, Long commentId,
                                         String username, String newText) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getTicket().getId().equals(ticket.getId())) {
            throw new NotFoundException("Comment does not belong to this ticket");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // only author or admin
        boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");
        if (!comment.getAuthor().getId().equals(user.getId()) && !isAdmin) {
            throw new RuntimeException("Access denied: cannot edit this comment");
        }

        comment.setText(newText);

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                saved.getText(),
                saved.getAuthor().getUsername(),
                saved.getCreatedAt().toString()
        );
    }

    // -----------------------------------------
    //             DELETE COMMENT
    // -----------------------------------------
    public void deleteComment(Long ticketId, Long commentId, String username) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getTicket().getId().equals(ticket.getId())) {
            throw new NotFoundException("Comment does not belong to this ticket");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // only author or admin
        boolean isAdmin = user.getRoles().contains("ROLE_ADMIN");
        if (!comment.getAuthor().getId().equals(user.getId()) && !isAdmin) {
            throw new RuntimeException("Access denied: cannot delete this comment");
        }

        commentRepository.delete(comment);
    }
}
