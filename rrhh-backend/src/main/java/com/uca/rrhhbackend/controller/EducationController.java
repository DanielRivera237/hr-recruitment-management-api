package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.EducationRequest;
import com.uca.rrhhbackend.dto.response.EducationResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CurrentUserService;
import com.uca.rrhhbackend.service.EducationService;

import jakarta.servlet.http.HttpServletRequest;
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
            @Valid @RequestBody EducationRequest request,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(educationService.create(currentUser, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<EducationResponse>> findMyEducation(
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.ok(educationService.findMyEducation(currentUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EducationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody EducationRequest request,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.ok(educationService.update(currentUser, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        educationService.delete(currentUser, id);

        return ResponseEntity.noContent().build();
    }

}