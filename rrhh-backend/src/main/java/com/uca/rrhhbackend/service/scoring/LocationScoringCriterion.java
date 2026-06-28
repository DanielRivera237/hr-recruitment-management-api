package com.uca.rrhhbackend.service.scoring;

import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.enums.JobModality;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class LocationScoringCriterion implements ScoringCriterion {

    @Override
    public BigDecimal calculate(
            CandidateProfile candidate,
            JobOffer jobOffer
    ) {
        if (jobOffer.getModality() == JobModality.REMOTE) {
            return new BigDecimal("20.00");
        }

        if (candidate.getAddress() == null) {
            return BigDecimal.ZERO;
        }

        boolean matches = candidate.getAddress()
                .toLowerCase()
                .contains(jobOffer.getLocation().toLowerCase());

        return matches
                ? new BigDecimal("20.00")
                : BigDecimal.ZERO;
    }
}