package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.EducationRequest;
import com.uca.rrhhbackend.dto.response.EducationResponse;
import com.uca.rrhhbackend.entity.CandidateProfile;
import com.uca.rrhhbackend.entity.Education;

public final class EducationMapper {

    private EducationMapper() {

    }

    public static Education toEntity(
            EducationRequest request,
            CandidateProfile candidateProfile
    ) {

        Education education = new Education();

        education.setCandidateProfile(candidateProfile);
        updateEntity(education, request);

        return education;
    }

    public static void updateEntity(
            Education education,
            EducationRequest request
    ) {

        boolean currentlyStudying = Boolean.TRUE.equals(request.currentlyStudying());

        education.setInstitution(request.institution().trim());
        education.setDegree(request.degree().trim());
        education.setFieldOfStudy(request.fieldOfStudy().trim());
        education.setStartDate(request.startDate());
        education.setEndDate(currentlyStudying ? null : request.endDate());
        education.setCurrentlyStudying(currentlyStudying);
    }

    public static EducationResponse toResponse(Education education) {

        return new EducationResponse(
                education.getId(),
                education.getInstitution(),
                education.getDegree(),
                education.getFieldOfStudy(),
                education.getStartDate(),
                education.getEndDate(),
                education.getCurrentlyStudying()
        );
    }

}