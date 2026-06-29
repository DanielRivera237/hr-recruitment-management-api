package com.uca.rrhhbackend.service.scoring;

import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.enums.JobModality;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LocationScoringCriterion implements ScoringCriterion {

    private static final BigDecimal MAX_SCORE =
            new BigDecimal("20.00");

    @Override
    public BigDecimal calculate(
            CandidateProfile candidate,
            JobOffer jobOffer
    ) {
        if (jobOffer.getModality() == JobModality.REMOTE) {
            return MAX_SCORE;
        }

        if (candidate.getAddress() == null
                || jobOffer.getLocation() == null) {
            return BigDecimal.ZERO;
        }

        boolean matches = candidate.getAddress()
                .toLowerCase()
                .contains(jobOffer.getLocation().toLowerCase());

        return matches ? MAX_SCORE : BigDecimal.ZERO;
    }
}