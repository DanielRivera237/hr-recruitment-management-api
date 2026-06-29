package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.response.RecruiterProfileResponse;
import com.uca.rrhhbackend.entity.RecruiterProfile;

public final class RecruiterProfileMapper {

    private RecruiterProfileMapper() {
    }

    public static RecruiterProfileResponse toResponse(
            RecruiterProfile recruiterProfile
    ) {
        return new RecruiterProfileResponse(
                recruiterProfile.getId(),
                recruiterProfile.getUser().getId(),
                recruiterProfile.getUser().getName(),
                recruiterProfile.getUser().getSurname(),
                recruiterProfile.getUser().getEmail(),
                recruiterProfile.getPosition(),
                recruiterProfile.getCompany().getId(),
                recruiterProfile.getCompany().getName(),
                recruiterProfile.getUser().getActive()
        );
    }
}