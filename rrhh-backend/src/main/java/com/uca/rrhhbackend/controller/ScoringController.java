package com.uca.rrhhbackend.controller;

import com.uca.rrhhbackend.dto.response.ScoringResponse;
import com.uca.rrhhbackend.service.CandidateScoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scoring")
public class ScoringController {

    private final CandidateScoringService scoringService;

    public ScoringController(
            CandidateScoringService scoringService
    ) {
        this.scoringService = scoringService;
    }

    @PostMapping("/applications/{applicationId}")
    public ResponseEntity<ScoringResponse> calculate(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(
                scoringService.calculate(applicationId)
        );
    }
}