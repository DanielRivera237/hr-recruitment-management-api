package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.request.SkillRequest;
import com.uca.rrhhbackend.dto.response.SkillResponse;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.service.CurrentUserService;
import com.uca.rrhhbackend.service.SkillService;

import jakarta.servlet.http.HttpServletRequest;
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
            @Valid @RequestBody SkillRequest request,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.addSkill(currentUser, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<SkillResponse>> findMySkills(
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        return ResponseEntity.ok(skillService.findMySkills(currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSkill(
            @PathVariable Long id,
            HttpServletRequest servletRequest
    ) {

        User currentUser = currentUserService.getCurrentUser(servletRequest);

        skillService.removeSkill(currentUser, id);

        return ResponseEntity.noContent().build();
    }

}