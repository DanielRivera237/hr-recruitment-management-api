package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.RecruiterCommentRequest;
import com.uca.rrhhbackend.dto.response.RecruiterCommentResponse;
import com.uca.rrhhbackend.service.RecruiterCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter-comments")
public class RecruiterCommentController {

    private final RecruiterCommentService commentService;

    public RecruiterCommentController(
            RecruiterCommentService commentService
    ) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<RecruiterCommentResponse> create(
            @Valid @RequestBody RecruiterCommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.create(request));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<RecruiterCommentResponse>> findByApplication(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(
                commentService.findByApplication(applicationId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
