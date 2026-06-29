package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.WorkExperienceRequest;
import com.uca.rrhhbackend.dto.response.WorkExperienceResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CurrentUserService;
import com.uca.rrhhbackend.service.WorkExperienceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/experience")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;
    private final CurrentUserService currentUserService;

    public WorkExperienceController(
            WorkExperienceService workExperienceService,
            CurrentUserService currentUserService
    ) {

        this.workExperienceService = workExperienceService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<WorkExperienceResponse> create(
            @Valid @RequestBody WorkExperienceRequest request,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workExperienceService.create(currentUser, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<WorkExperienceResponse>> findMyWorkExperience(
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.ok(
                workExperienceService.findMyWorkExperience(currentUser)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkExperienceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody WorkExperienceRequest request,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.ok(
                workExperienceService.update(currentUser, id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        workExperienceService.delete(currentUser, id);

        return ResponseEntity.noContent().build();
    }

}