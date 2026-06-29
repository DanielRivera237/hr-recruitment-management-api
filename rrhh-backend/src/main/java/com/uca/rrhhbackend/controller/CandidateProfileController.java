package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.CandidateProfileRequest;
import com.uca.rrhhbackend.dto.response.CandidateProfileResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CandidateProfileService;
import com.uca.rrhhbackend.service.CurrentUserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates/profile")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;
    private final CurrentUserService currentUserService;

    public CandidateProfileController(
            CandidateProfileService candidateProfileService,
            CurrentUserService currentUserService
    ) {
        this.candidateProfileService = candidateProfileService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/me")
    public ResponseEntity<CandidateProfileResponse> createMyProfile(
            @Valid @RequestBody CandidateProfileRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        CandidateProfileResponse response =
                candidateProfileService.createOrUpdateMyProfile(
                        currentUser,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CandidateProfileResponse> getMyProfile() {

        User currentUser = currentUserService.getCurrentUser();

        CandidateProfileResponse response =
                candidateProfileService.getMyProfile(currentUser);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<CandidateProfileResponse> updateMyProfile(
            @Valid @RequestBody CandidateProfileRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        CandidateProfileResponse response =
                candidateProfileService.createOrUpdateMyProfile(
                        currentUser,
                        request
                );

        return ResponseEntity.ok(response);
    }
}