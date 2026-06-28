package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.WorkExperienceRequest;
import com.uca.rrhhbackend.dto.response.WorkExperienceResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.entity.WorkExperience;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.WorkExperienceMapper;
import com.uca.rrhhbackend.repository.WorkExperienceRepository;
import com.uca.rrhhbackend.service.CandidateProfileService;
import com.uca.rrhhbackend.service.WorkExperienceService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WorkExperienceServiceImpl implements WorkExperienceService {

    private final WorkExperienceRepository workExperienceRepository;
    private final CandidateProfileService candidateProfileService;

    public WorkExperienceServiceImpl(
            WorkExperienceRepository workExperienceRepository,
            CandidateProfileService candidateProfileService
    ) {

        this.workExperienceRepository = workExperienceRepository;
        this.candidateProfileService = candidateProfileService;
    }

    @Override
    public WorkExperienceResponse create(
            User currentUser,
            WorkExperienceRequest request
    ) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        validateDates(
                request.startDate(),
                request.endDate(),
                request.currentlyWorking()
        );

        WorkExperience workExperience = WorkExperienceMapper.toEntity(request, profile);

        return WorkExperienceMapper.toResponse(
                workExperienceRepository.save(workExperience)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkExperienceResponse> findMyWorkExperience(User currentUser) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        return workExperienceRepository.findByCandidateProfileId(profile.getId())
                .stream()
                .map(WorkExperienceMapper::toResponse)
                .toList();
    }

    @Override
    public WorkExperienceResponse update(
            User currentUser,
            Long workExperienceId,
            WorkExperienceRequest request
    ) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        validateDates(
                request.startDate(),
                request.endDate(),
                request.currentlyWorking()
        );

        WorkExperience workExperience = workExperienceRepository
                .findByIdAndCandidateProfileId(workExperienceId, profile.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontro la expericia laboral"
                        )
                );

        WorkExperienceMapper.updateEntity(workExperience, request);

        return WorkExperienceMapper.toResponse(
                workExperienceRepository.save(workExperience)
        );
    }

    @Override
    public void delete(User currentUser, Long workExperienceId) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        WorkExperience workExperience = workExperienceRepository
                .findByIdAndCandidateProfileId(workExperienceId, profile.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la experiencia laboral"
                        )
                );

        workExperienceRepository.delete(workExperience);
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate,
            Boolean currentlyWorking
    ) {

        if (startDate == null) {
            throw new BusinessException("La fecha de inicio es obligatoria");
        }

        if (
                !Boolean.TRUE.equals(currentlyWorking)
                        && endDate != null
                        && endDate.isBefore(startDate)
        ) {
            throw new BusinessException(
                    "La fecha de finalización no puede ser anterior a la fecha de inicio"
            );
        }
    }

}