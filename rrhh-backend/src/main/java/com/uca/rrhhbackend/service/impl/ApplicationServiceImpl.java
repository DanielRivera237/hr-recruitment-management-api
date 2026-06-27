package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.ApplicationRequest;
import com.uca.rrhhbackend.dto.request.ApplicationStatusRequest;
import com.uca.rrhhbackend.dto.response.ApplicationResponse;
import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.enums.ApplicationStatus;
import com.uca.rrhhbackend.entity.enums.JobOfferStatus;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ConflictException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.ApplicationMapper;
import com.uca.rrhhbackend.repository.ApplicationRepository;
import com.uca.rrhhbackend.repository.CandidateProfileRepository;
import com.uca.rrhhbackend.repository.JobOfferRepository;
import com.uca.rrhhbackend.service.ApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobOfferRepository jobOfferRepository;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            CandidateProfileRepository candidateProfileRepository,
            JobOfferRepository jobOfferRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.jobOfferRepository = jobOfferRepository;
    }

    @Override
    public ApplicationResponse create(ApplicationRequest request) {
        CandidateProfile candidate = candidateProfileRepository
                .findById(request.candidateProfileId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Candidato no encontrado")
                );

        JobOffer jobOffer = jobOfferRepository
                .findById(request.jobOfferId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vacante no encontrada")
                );

        if (jobOffer.getStatus() != JobOfferStatus.PUBLISHED) {
            throw new BusinessException(
                    "Solo se puede postular a vacantes publicadas"
            );
        }

        if (applicationRepository
                .existsByCandidateProfileIdAndJobOfferId(
                        candidate.getId(),
                        jobOffer.getId()
                )) {
            throw new ConflictException(
                    "El candidato ya se postuló a esta vacante"
            );
        }

        Application application = ApplicationMapper.toEntity(request);
        application.setCandidateProfile(candidate);
        application.setJobOffer(jobOffer);
        application.setStatus(ApplicationStatus.APPLIED);

        return ApplicationMapper.toResponse(
                applicationRepository.save(application)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findAll() {
        return applicationRepository.findAll()
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse findById(Long id) {
        return ApplicationMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findByCandidate(Long candidateProfileId) {
        return applicationRepository
                .findByCandidateProfileId(candidateProfileId)
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findByJobOffer(Long jobOfferId) {
        return applicationRepository.findByJobOfferId(jobOfferId)
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    public ApplicationResponse updateStatus(
            Long id,
            ApplicationStatusRequest request
    ) {
        Application application = findEntity(id);

        validateStatusTransition(
                application.getStatus(),
                request.status()
        );

        application.setStatus(request.status());

        return ApplicationMapper.toResponse(
                applicationRepository.save(application)
        );
    }

    private Application findEntity(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Postulación no encontrada con id " + id
                        )
                );
    }

    private void validateStatusTransition(
            ApplicationStatus current,
            ApplicationStatus next
    ) {
        if (current == ApplicationStatus.REJECTED
                || current == ApplicationStatus.HIRED) {
            throw new BusinessException(
                    "Una postulación finalizada no puede cambiar de estado"
            );
        }

        if (current == next) {
            throw new BusinessException(
                    "La postulación ya tiene ese estado"
            );
        }

        boolean valid =
                (current == ApplicationStatus.APPLIED
                        && (next == ApplicationStatus.REVIEWED
                        || next == ApplicationStatus.REJECTED))
                        ||
                        (current == ApplicationStatus.REVIEWED
                                && (next == ApplicationStatus.TECHNICAL_INTERVIEW
                                || next == ApplicationStatus.REJECTED))
                        ||
                        (current == ApplicationStatus.TECHNICAL_INTERVIEW
                                && (next == ApplicationStatus.HIRED
                                || next == ApplicationStatus.REJECTED));

        if (!valid) {
            throw new BusinessException(
                    "Transición de estado no permitida"
            );
        }
    }
}