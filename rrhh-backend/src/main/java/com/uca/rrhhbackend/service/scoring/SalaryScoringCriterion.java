package com.uca.rrhhbackend.service.scoring;

import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SalaryScoringCriterion implements ScoringCriterion {

    @Override
    public BigDecimal calculate(
            CandidateProfile candidate,
            JobOffer jobOffer
    ) {
        if (candidate.getSalaryExpectation() == null) {
            return new BigDecimal("10.00");
        }

        if (candidate.getSalaryExpectation()
                .compareTo(jobOffer.getSalaryMax()) <= 0) {
            return new BigDecimal("20.00");
        }

        return BigDecimal.ZERO;
    }
}