package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.InterviewRequest;
import com.uca.rrhhbackend.dto.request.InterviewStatusRequest;
import com.uca.rrhhbackend.dto.response.InterviewResponse;
import com.uca.rrhhbackend.entity.Application;
import com.uca.rrhhbackend.entity.Interview;
import com.uca.rrhhbackend.entity.RecruiterProfile;
import com.uca.rrhhbackend.entity.enums.ApplicationStatus;
import com.uca.rrhhbackend.entity.enums.InterviewStatus;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.InterviewMapper;
import com.uca.rrhhbackend.repository.ApplicationRepository;
import com.uca.rrhhbackend.repository.InterviewRepository;
import com.uca.rrhhbackend.repository.RecruiterProfileRepository;
import com.uca.rrhhbackend.service.InterviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public InterviewServiceImpl(
            InterviewRepository interviewRepository,
            ApplicationRepository applicationRepository,
            RecruiterProfileRepository recruiterProfileRepository
    ) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    @Override
    public InterviewResponse create(InterviewRequest request) {
        validateFutureDate(request.scheduledDate());

        Application application = applicationRepository
                .findById(request.applicationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulación no encontrada")
                );

        RecruiterProfile recruiter = recruiterProfileRepository
                .findById(request.recruiterProfileId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reclutador no encontrado")
                );

        if (application.getStatus() != ApplicationStatus.REVIEWED
                && application.getStatus() != ApplicationStatus.TECHNICAL_INTERVIEW) {
            throw new BusinessException(
                    "La postulación no está en una etapa válida para entrevista"
            );
        }

        if (!application.getJobOffer()
                .getCompany()
                .getId()
                .equals(recruiter.getCompany().getId())) {
            throw new BusinessException(
                    "El reclutador no pertenece a la empresa de la vacante"
            );
        }

        Interview interview = InterviewMapper.toEntity(request);
        interview.setApplication(application);
        interview.setRecruiterProfile(recruiter);
        interview.setStatus(InterviewStatus.SCHEDULED);

        return InterviewMapper.toResponse(
                interviewRepository.save(interview)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> findAll() {
        return interviewRepository.findAll()
                .stream()
                .map(InterviewMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewResponse findById(Long id) {
        return InterviewMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterviewResponse> findByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId)
                .stream()
                .map(InterviewMapper::toResponse)
                .toList();
    }

    @Override
    public InterviewResponse update(
            Long id,
            InterviewRequest request
    ) {
        validateFutureDate(request.scheduledDate());

        Interview interview = findEntity(id);

        if (interview.getStatus() == InterviewStatus.COMPLETED
                || interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new BusinessException(
                    "No se puede modificar una entrevista finalizada o cancelada"
            );
        }

        Application application = applicationRepository
                .findById(request.applicationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Postulación no encontrada")
                );

        RecruiterProfile recruiter = recruiterProfileRepository
                .findById(request.recruiterProfileId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reclutador no encontrado")
                );

        InterviewMapper.updateEntity(interview, request);
        interview.setApplication(application);
        interview.setRecruiterProfile(recruiter);
        interview.setStatus(InterviewStatus.RESCHEDULED);

        return InterviewMapper.toResponse(
                interviewRepository.save(interview)
        );
    }

    @Override
    public InterviewResponse updateStatus(
            Long id,
            InterviewStatusRequest request
    ) {
        Interview interview = findEntity(id);

        if (interview.getStatus() == InterviewStatus.COMPLETED
                || interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new BusinessException(
                    "La entrevista ya está finalizada"
            );
        }

        interview.setStatus(request.status());

        return InterviewMapper.toResponse(
                interviewRepository.save(interview)
        );
    }

    private Interview findEntity(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Entrevista no encontrada con id " + id
                        )
                );
    }

    private void validateFutureDate(LocalDateTime date) {
        if (!date.isAfter(LocalDateTime.now())) {
            throw new BusinessException(
                    "La fecha de entrevista debe ser futura"
            );
        }
    }
}