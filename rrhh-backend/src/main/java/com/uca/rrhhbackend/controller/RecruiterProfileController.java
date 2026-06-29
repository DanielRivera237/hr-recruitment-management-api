package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.RecruiterProfileRequest;
import com.uca.rrhhbackend.dto.response.RecruiterProfileResponse;
import com.uca.rrhhbackend.service.RecruiterProfileService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterProfileController {

    private final RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(
            RecruiterProfileService recruiterProfileService
    ) {
        this.recruiterProfileService =
                recruiterProfileService;
    }

    @PostMapping
    public ResponseEntity<RecruiterProfileResponse> create(
            @Valid
            @RequestBody RecruiterProfileRequest request
    ) {
        RecruiterProfileResponse response =
                recruiterProfileService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RecruiterProfileResponse>>
    findAll() {
        return ResponseEntity.ok(
                recruiterProfileService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruiterProfileResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                recruiterProfileService.findById(id)
        );
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<RecruiterProfileResponse>>
    findByCompany(
            @PathVariable Long companyId
    ) {
        return ResponseEntity.ok(
                recruiterProfileService.findByCompany(
                        companyId
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecruiterProfileResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody RecruiterProfileRequest request
    ) {
        return ResponseEntity.ok(
                recruiterProfileService.update(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {
        recruiterProfileService.deactivate(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}