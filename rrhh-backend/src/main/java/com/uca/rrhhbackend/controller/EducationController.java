package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.EducationRequest;
import com.uca.rrhhbackend.dto.response.EducationResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CurrentUserService;
import com.uca.rrhhbackend.service.EducationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/education")
public class EducationController {

    private final EducationService educationService;
    private final CurrentUserService currentUserService;

    public EducationController(
            EducationService educationService,
            CurrentUserService currentUserService
    ) {
        this.educationService = educationService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<EducationResponse> create(
            @Valid @RequestBody EducationRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        EducationResponse response =
                educationService.create(currentUser, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<EducationResponse>> findMyEducation() {

        User currentUser = currentUserService.getCurrentUser();

        List<EducationResponse> response =
                educationService.findMyEducation(currentUser);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EducationRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        EducationResponse response =
                educationService.update(currentUser, id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        User currentUser = currentUserService.getCurrentUser();

        educationService.delete(currentUser, id);

        return ResponseEntity.noContent().build();
    }
}