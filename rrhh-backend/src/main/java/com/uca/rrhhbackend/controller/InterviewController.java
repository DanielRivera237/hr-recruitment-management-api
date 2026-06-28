package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.InterviewRequest;
import com.uca.rrhhbackend.dto.request.InterviewStatusRequest;
import com.uca.rrhhbackend.dto.response.InterviewResponse;
import com.uca.rrhhbackend.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public ResponseEntity<InterviewResponse> create(
            @Valid @RequestBody InterviewRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(interviewService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<InterviewResponse>> findAll() {
        return ResponseEntity.ok(interviewService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(interviewService.findById(id));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponse>> findByApplication(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(
                interviewService.findByApplication(applicationId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request
    ) {
        return ResponseEntity.ok(
                interviewService.update(id, request)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<InterviewResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody InterviewStatusRequest request
    ) {
        return ResponseEntity.ok(
                interviewService.updateStatus(id, request)
        );
    }
}