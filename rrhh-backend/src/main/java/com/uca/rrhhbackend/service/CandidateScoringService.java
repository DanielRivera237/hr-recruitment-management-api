package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.response.ScoringResponse;

public interface CandidateScoringService {

    ScoringResponse calculate(Long applicationId);
}