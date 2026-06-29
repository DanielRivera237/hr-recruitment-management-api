package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.SkillRequest;
import com.uca.rrhhbackend.dto.response.SkillResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CurrentUserService;
import com.uca.rrhhbackend.service.SkillService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates/skills")
public class SkillController {

    private final SkillService skillService;
    private final CurrentUserService currentUserService;

    public SkillController(
            SkillService skillService,
            CurrentUserService currentUserService
    ) {
        this.skillService = skillService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<SkillResponse> addSkill(
            @Valid @RequestBody SkillRequest request
    ) {

        User currentUser = currentUserService.getCurrentUser();

        SkillResponse response =
                skillService.addSkill(currentUser, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<SkillResponse>> findMySkills() {

        User currentUser = currentUserService.getCurrentUser();

        List<SkillResponse> response =
                skillService.findMySkills(currentUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSkill(
            @PathVariable Long id
    ) {

        User currentUser = currentUserService.getCurrentUser();

        skillService.removeSkill(currentUser, id);

        return ResponseEntity.noContent().build();
    }
}