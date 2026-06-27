package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.JobOfferRequest;
import com.uca.rrhhbackend.dto.response.JobOfferResponse;
import com.uca.rrhhbackend.service.JobOfferService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    private final JobOfferService jobOfferService;

    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @PostMapping
    public ResponseEntity<JobOfferResponse> create(
            @Valid @RequestBody JobOfferRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobOfferService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<JobOfferResponse>> findAll() {
        return ResponseEntity.ok(jobOfferService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOfferResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody JobOfferRequest request
    ) {
        return ResponseEntity.ok(jobOfferService.update(id, request));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<JobOfferResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.publish(id));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<JobOfferResponse> close(@PathVariable Long id) {
        return ResponseEntity.ok(jobOfferService.close(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        jobOfferService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}