package com.uca.rrhhbackend.service.scoring;

import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;

import java.math.BigDecimal;

public interface ScoringCriterion {

    BigDecimal calculate(
            CandidateProfile candidate,
            JobOffer jobOffer
    );
}