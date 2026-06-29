package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.ApplicationRequest;
import com.uca.rrhhbackend.dto.request.ApplicationStatusRequest;
import com.uca.rrhhbackend.dto.response.ApplicationResponse;
import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.JobOffer;
import com.uca.rrhhbackend.entity.User;
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
import com.uca.rrhhbackend.service.CurrentUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobOfferRepository jobOfferRepository;
    private final CurrentUserService currentUserService;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            CandidateProfileRepository candidateProfileRepository,
            JobOfferRepository jobOfferRepository,
            CurrentUserService currentUserService
    ) {
        this.applicationRepository = applicationRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.jobOfferRepository = jobOfferRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public ApplicationResponse create(
            User currentUser,
            ApplicationRequest request
    ) {
        currentUserService.requireCandidate(currentUser);

        CandidateProfile candidate = findCandidateByUser(currentUser);

        JobOffer jobOffer = jobOfferRepository
                .findById(request.jobOfferId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vacante no encontrada"
                        )
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
                    "Ya te postulaste a esta vacante"
            );
        }

        Application application = new Application();
        application.setCandidateProfile(candidate);
        application.setJobOffer(jobOffer);
        application.setCoverLetter(request.coverLetter());
        application.setStatus(ApplicationStatus.APPLIED);

        Application savedApplication =
                applicationRepository.save(application);

        return ApplicationMapper.toResponse(savedApplication);
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
    public ApplicationResponse findMyApplicationById(
            User currentUser,
            Long id
    ) {
        currentUserService.requireCandidate(currentUser);

        CandidateProfile candidate = findCandidateByUser(currentUser);
        Application application = findEntity(id);

        validateApplicationOwnership(application, candidate);

        return ApplicationMapper.toResponse(application);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findMyApplications(
            User currentUser
    ) {
        currentUserService.requireCandidate(currentUser);

        CandidateProfile candidate = findCandidateByUser(currentUser);

        return applicationRepository
                .findByCandidateProfileId(candidate.getId())
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findByCandidate(
            Long candidateProfileId
    ) {
        if (!candidateProfileRepository.existsById(candidateProfileId)) {
            throw new ResourceNotFoundException(
                    "Candidato no encontrado"
            );
        }

        return applicationRepository
                .findByCandidateProfileId(candidateProfileId)
                .stream()
                .map(ApplicationMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findByJobOffer(
            Long jobOfferId
    ) {
        if (!jobOfferRepository.existsById(jobOfferId)) {
            throw new ResourceNotFoundException(
                    "Vacante no encontrada"
            );
        }

        return applicationRepository
                .findByJobOfferId(jobOfferId)
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

        Application savedApplication =
                applicationRepository.save(application);

        return ApplicationMapper.toResponse(savedApplication);
    }

    private CandidateProfile findCandidateByUser(User currentUser) {
        return candidateProfileRepository
                .findByUserId(currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El candidato no tiene perfil"
                        )
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

    private void validateApplicationOwnership(
            Application application,
            CandidateProfile candidate
    ) {
        if (!application
                .getCandidateProfile()
                .getId()
                .equals(candidate.getId())) {

            throw new BusinessException(
                    "No tienes permiso para consultar esta postulación"
            );
        }
    }

    private void validateStatusTransition(
            ApplicationStatus current,
            ApplicationStatus next
    ) {
        if (next == null) {
            throw new BusinessException(
                    "El nuevo estado es obligatorio"
            );
        }

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
                        && (
                        next == ApplicationStatus.REVIEWED
                                || next == ApplicationStatus.REJECTED
                ))
                        ||
                        (current == ApplicationStatus.REVIEWED
                                && (
                                next == ApplicationStatus.TECHNICAL_INTERVIEW
                                        || next == ApplicationStatus.REJECTED
                        ))
                        ||
                        (current == ApplicationStatus.TECHNICAL_INTERVIEW
                                && (
                                next == ApplicationStatus.HIRED
                                        || next == ApplicationStatus.REJECTED
                        ));

        if (!valid) {
            throw new BusinessException(
                    "Transición de estado no permitida"
            );
        }
    }
}