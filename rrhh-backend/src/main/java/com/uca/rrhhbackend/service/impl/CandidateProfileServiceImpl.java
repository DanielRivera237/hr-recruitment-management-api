package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.CandidateProfileRequest;
import com.uca.rrhhbackend.dto.response.CandidateProfileResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.exception.BusinessException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.CandidateProfileMapper;
import com.uca.rrhhbackend.repository.CandidateProfileRepository;
import com.uca.rrhhbackend.repository.EducationRepository;
import com.uca.rrhhbackend.repository.WorkExperienceRepository;
import com.uca.rrhhbackend.service.CandidateProfileService;
import com.uca.rrhhbackend.service.CurrentUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final CurrentUserService currentUserService;

    public CandidateProfileServiceImpl(
            CandidateProfileRepository candidateProfileRepository,
            EducationRepository educationRepository,
            WorkExperienceRepository workExperienceRepository,
            CurrentUserService currentUserService
    ) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.educationRepository = educationRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public CandidateProfileResponse createOrUpdateMyProfile(
            User currentUser,
            CandidateProfileRequest request
    ) {

        currentUserService.requireCandidate(currentUser);
        validateProfileRequest(request);

        CandidateProfile profile = candidateProfileRepository
                .findByUserId(currentUser.getId())
                .orElseGet(() -> {
                    CandidateProfile newProfile = new CandidateProfile();
                    newProfile.setUser(currentUser);
                    return newProfile;
                });

        CandidateProfileMapper.updateEntity(profile, request);

        CandidateProfile savedProfile = candidateProfileRepository.save(profile);

        return buildProfileResponse(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateProfileResponse getMyProfile(User currentUser) {

        CandidateProfile profile = getMyProfileEntity(currentUser);

        return buildProfileResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public CandidateProfile getMyProfileEntity(User currentUser) {

        currentUserService.requireCandidate(currentUser);

        return candidateProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("El candidato no tiene perfil")
                );
    }

    private CandidateProfileResponse buildProfileResponse(CandidateProfile profile) {

        return CandidateProfileMapper.toResponse(
                profile,
                educationRepository.findByCandidateProfileId(profile.getId()),
                workExperienceRepository.findByCandidateProfileId(profile.getId())
        );
    }

    private void validateProfileRequest(CandidateProfileRequest request) {

        if (request.yearsExperience() != null && request.yearsExperience() < 0) {
            throw new BusinessException("Los años de experiencia deben ser positivos");
        }

        if (
                request.salaryExpectation() != null
                        && request.salaryExpectation().compareTo(BigDecimal.ZERO) < 0
        ) {
            throw new BusinessException("La expectativa salarial debe ser positiva");
        }
    }

}