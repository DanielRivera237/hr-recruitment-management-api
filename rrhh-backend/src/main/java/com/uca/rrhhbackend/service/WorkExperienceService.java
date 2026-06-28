package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.WorkExperienceRequest;
import com.uca.rrhhbackend.dto.response.WorkExperienceResponse;
import com.uca.rrhhbackend.entity.User;

import java.util.List;

public interface WorkExperienceService {
    WorkExperienceResponse create(
            User currentUser,
            WorkExperienceRequest request
    );

    List<WorkExperienceResponse> findMyWorkExperience(User currentUser);

    WorkExperienceResponse update(
            User currentUser,
            Long workExperienceId,
            WorkExperienceRequest request
    );

    void delete(User currentUser, Long workExperienceId);
}