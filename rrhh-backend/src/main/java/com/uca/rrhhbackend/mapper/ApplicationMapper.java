package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.ApplicationRequest;
import com.uca.rrhhbackend.dto.response.ApplicationResponse;
import com.uca.rrhhbackend.entity.Application;

public final class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static Application toEntity(ApplicationRequest request) {
        Application application = new Application();
        application.setCoverLetter(request.coverLetter());
        return application;
    }

    public static ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
                application.getId(),
                application.getApplicationDate(),
                application.getCoverLetter(),
                application.getStatus(),
                application.getScore(),
                application.getCandidateProfile().getId(),
                application.getCandidateProfile().getUser().getName()
                        + " "
                        + application.getCandidateProfile().getUser().getSurname(),
                application.getJobOffer().getId(),
                application.getJobOffer().getTitle()
        );
    }
}