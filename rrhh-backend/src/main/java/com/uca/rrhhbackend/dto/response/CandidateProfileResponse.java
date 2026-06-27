package com.uca.rrhhbackend.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CandidateProfileResponse(
        Long id,
        Long userId,
        String fullName,
        String email,
        String phone,
        String address,
        String professionalSummary,
        Integer yearsExperience,
        BigDecimal salaryExpectation,
        String availability,
        List<SkillResponse> skills,
        List<EducationResponse> education,
        List<WorkExperienceResponse> workExperience
) {
}