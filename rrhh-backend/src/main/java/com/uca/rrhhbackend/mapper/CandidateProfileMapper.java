package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.CandidateProfileRequest;
import com.uca.rrhhbackend.dto.response.CandidateProfileResponse;
import com.uca.rrhhbackend.dto.response.EducationResponse;
import com.uca.rrhhbackend.dto.response.SkillResponse;
import com.uca.rrhhbackend.dto.response.WorkExperienceResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.Education;
import com.uca.rrhhbackend.entity.User;
import com.uca.rrhhbackend.entity.WorkExperience;

import java.util.List;

public final class CandidateProfileMapper {

    private CandidateProfileMapper() {

    }

    public static void updateEntity(
            CandidateProfile profile,
            CandidateProfileRequest request
    ) {

        profile.setPhone(request.phone());
        profile.setAddress(request.address());
        profile.setProfessionalSummary(request.professionalSummary());
        profile.setYearsExperience(
                request.yearsExperience() == null ? 0 : request.yearsExperience()
        );
        profile.setSalaryExpectation(request.salaryExpectation());
        profile.setAvailability(request.availability());
    }

    public static CandidateProfileResponse toResponse(
            CandidateProfile profile,
            List<Education> education,
            List<WorkExperience> workExperience
    ) {

        User user = profile.getUser();

        List<SkillResponse> skillResponses = profile.getSkills()
                .stream()
                .map(SkillMapper::toResponse)
                .toList();

        List<EducationResponse> educationResponses = education
                .stream()
                .map(EducationMapper::toResponse)
                .toList();

        List<WorkExperienceResponse> workExperienceResponses = workExperience
                .stream()
                .map(WorkExperienceMapper::toResponse)
                .toList();

        return new CandidateProfileResponse(
                profile.getId(),
                user.getId(),
                buildFullName(user),
                user.getEmail(),
                profile.getPhone(),
                profile.getAddress(),
                profile.getProfessionalSummary(),
                profile.getYearsExperience(),
                profile.getSalaryExpectation(),
                profile.getAvailability(),
                skillResponses,
                educationResponses,
                workExperienceResponses
        );
    }

    private static String buildFullName(User user) {

        return (user.getName() + " " + user.getSurname()).trim();
    }

}