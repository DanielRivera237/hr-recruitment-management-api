package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.TechnicalTestRequest;
import com.uca.rrhhbackend.dto.request.TechnicalTestResultRequest;
import com.uca.rrhhbackend.dto.response.TechnicalTestResponse;
import com.uca.rrhhbackend.service.TechnicalTestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technical-tests")
public class TechnicalTestController {

    private final TechnicalTestService technicalTestService;

    public TechnicalTestController(
            TechnicalTestService technicalTestService
    ) {
        this.technicalTestService = technicalTestService;
    }

    @PostMapping
    public ResponseEntity<TechnicalTestResponse> create(
            @Valid @RequestBody TechnicalTestRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(technicalTestService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicalTestResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                technicalTestService.findById(id)
        );
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<TechnicalTestResponse>> findByApplication(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(
                technicalTestService.findByApplication(applicationId)
        );
    }

    @PatchMapping("/{id}/submit")
    public ResponseEntity<TechnicalTestResponse> submit(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                technicalTestService.submit(id)
        );
    }

    @PatchMapping("/{id}/review")
    public ResponseEntity<TechnicalTestResponse> review(
            @PathVariable Long id,
            @Valid @RequestBody TechnicalTestResultRequest request
    ) {
        return ResponseEntity.ok(
                technicalTestService.review(id, request)
        );
    }
}