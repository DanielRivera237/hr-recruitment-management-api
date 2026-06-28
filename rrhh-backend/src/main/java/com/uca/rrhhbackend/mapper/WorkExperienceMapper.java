package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.WorkExperienceRequest;
import com.uca.rrhhbackend.dto.response.WorkExperienceResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.WorkExperience;

public final class WorkExperienceMapper {

    private WorkExperienceMapper() {

    }

    public static WorkExperience toEntity(
            WorkExperienceRequest request,
            CandidateProfile candidateProfile
    ) {

        WorkExperience workExperience = new WorkExperience();

        workExperience.setCandidateProfile(candidateProfile);
        updateEntity(workExperience, request);

        return workExperience;
    }

    public static void updateEntity(
            WorkExperience workExperience,
            WorkExperienceRequest request
    ) {

        boolean currentlyWorking = Boolean.TRUE.equals(request.currentlyWorking());

        workExperience.setCompanyName(request.companyName().trim());
        workExperience.setPosition(request.position().trim());
        workExperience.setDescription(request.description());
        workExperience.setStartDate(request.startDate());
        workExperience.setEndDate(currentlyWorking ? null : request.endDate());
        workExperience.setCurrentlyWorking(currentlyWorking);
    }

    public static WorkExperienceResponse toResponse(WorkExperience workExperience) {

        return new WorkExperienceResponse(
                workExperience.getId(),
                workExperience.getCompanyName(),
                workExperience.getPosition(),
                workExperience.getDescription(),
                workExperience.getStartDate(),
                workExperience.getEndDate(),
                workExperience.getCurrentlyWorking()
        );
    }

}