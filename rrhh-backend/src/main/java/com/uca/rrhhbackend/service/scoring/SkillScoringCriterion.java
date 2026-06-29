package com.uca.rrhhbackend.service.scoring;

import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.Skill;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SkillScoringCriterion implements ScoringCriterion {

    private static final BigDecimal MAX_SCORE =
            new BigDecimal("60.00");

    @Override
    public BigDecimal calculate(
            CandidateProfile candidate,
            JobOffer jobOffer
    ) {
        if (jobOffer.getRequiredSkills() == null
                || jobOffer.getRequiredSkills().isEmpty()) {
            return MAX_SCORE;
        }

        Set<Long> candidateSkillIds = candidate.getSkills()
                .stream()
                .map(Skill::getId)
                .collect(Collectors.toSet());

        long matches = jobOffer.getRequiredSkills()
                .stream()
                .map(Skill::getId)
                .filter(candidateSkillIds::contains)
                .count();

        return BigDecimal.valueOf(matches)
                .divide(
                        BigDecimal.valueOf(
                                jobOffer.getRequiredSkills().size()
                        ),
                        4,
                        RoundingMode.HALF_UP
                )
                .multiply(MAX_SCORE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}