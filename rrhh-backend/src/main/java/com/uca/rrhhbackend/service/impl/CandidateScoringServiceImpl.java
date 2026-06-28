package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.response.ScoringResponse;
import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.repository.ApplicationRepository;
import com.uca.rrhhbackend.service.CandidateScoringService;
import com.uca.rrhhbackend.service.scoring.ScoringCriterion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class CandidateScoringServiceImpl
        implements CandidateScoringService {

    private final ApplicationRepository applicationRepository;
    private final List<ScoringCriterion> criteria;

    public CandidateScoringServiceImpl(
            ApplicationRepository applicationRepository,
            List<ScoringCriterion> criteria
    ) {
        this.applicationRepository = applicationRepository;
        this.criteria = criteria;
    }

    @Override
    public ScoringResponse calculate(Long applicationId) {
        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Postulación no encontrada con id "
                                        + applicationId
                        )
                );

        BigDecimal score = criteria.stream()
                .map(criterion -> criterion.calculate(
                        application.getCandidateProfile(),
                        application.getJobOffer()
                ))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .min(new BigDecimal("100.00"))
                .setScale(2, RoundingMode.HALF_UP);

        application.setScore(score);
        applicationRepository.save(application);

        return new ScoringResponse(
                application.getId(),
                score
        );
    }
}