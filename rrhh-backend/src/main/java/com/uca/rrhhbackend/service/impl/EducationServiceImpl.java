package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.EducationRequest;
import com.uca.rrhhbackend.dto.response.EducationResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.Education;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.EducationMapper;
import com.uca.rrhhbackend.repository.EducationRepository;
import com.uca.rrhhbackend.service.CandidateProfileService;
import com.uca.rrhhbackend.service.EducationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final CandidateProfileService candidateProfileService;

    public EducationServiceImpl(
            EducationRepository educationRepository,
            CandidateProfileService candidateProfileService
    ) {

        this.educationRepository = educationRepository;
        this.candidateProfileService = candidateProfileService;
    }

    @Override
    public EducationResponse create(
            User currentUser,
            EducationRequest request
    ) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        validateDates(
                request.startDate(),
                request.endDate(),
                request.currentlyStudying()
        );

        Education education = EducationMapper.toEntity(request, profile);

        return EducationMapper.toResponse(educationRepository.save(education));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EducationResponse> findMyEducation(User currentUser) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        return educationRepository.findByCandidateProfileId(profile.getId())
                .stream()
                .map(EducationMapper::toResponse)
                .toList();
    }

    @Override
    public EducationResponse update(
            User currentUser,
            Long educationId,
            EducationRequest request
    ) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        validateDates(
                request.startDate(),
                request.endDate(),
                request.currentlyStudying()
        );

        Education education = educationRepository
                .findByIdAndCandidateProfileId(educationId, profile.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontró educacion con id " + educationId)
                );

        EducationMapper.updateEntity(education, request);

        return EducationMapper.toResponse(educationRepository.save(education));
    }

    @Override
    public void delete(User currentUser, Long educationId) {

        CandidateProfile profile = candidateProfileService.getMyProfileEntity(currentUser);

        Education education = educationRepository
                .findByIdAndCandidateProfileId(educationId, profile.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No se encontro edudacion con id " + educationId)
                );

        educationRepository.delete(education);
    }

    private void validateDates(
            LocalDate startDate,
            LocalDate endDate,
            Boolean currentlyStudying
    ) {

        if (startDate == null) {
            throw new BusinessException("La fecha de inicio es obligatoria");
        }

        if (
                !Boolean.TRUE.equals(currentlyStudying)
                        && endDate != null
                        && endDate.isBefore(startDate)
        ) {
            throw new BusinessException(
                    "La fecha de finalización no puede ser anterior a la fecha de inicio"
            );
        }
    }

}