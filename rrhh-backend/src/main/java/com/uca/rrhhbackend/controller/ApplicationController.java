package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.ApplicationRequest;
import com.uca.rrhhbackend.dto.request.ApplicationStatusRequest;
import com.uca.rrhhbackend.dto.response.ApplicationResponse;
import com.uca.rrhhbackend.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody ApplicationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(applicationService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ApplicationResponse>> findAll() {
        return ResponseEntity.ok(applicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(applicationService.findById(id));
    }

    @GetMapping("/candidate/{candidateProfileId}")
    public ResponseEntity<List<ApplicationResponse>> findByCandidate(
            @PathVariable Long candidateProfileId
    ) {
        return ResponseEntity.ok(
                applicationService.findByCandidate(candidateProfileId)
        );
    }

    @GetMapping("/job-offer/{jobOfferId}")
    public ResponseEntity<List<ApplicationResponse>> findByJobOffer(
            @PathVariable Long jobOfferId
    ) {
        return ResponseEntity.ok(
                applicationService.findByJobOffer(jobOfferId)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusRequest request
    ) {
        return ResponseEntity.ok(
                applicationService.updateStatus(id, request)
        );
    }
}