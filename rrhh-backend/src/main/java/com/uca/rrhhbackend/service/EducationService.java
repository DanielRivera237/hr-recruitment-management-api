package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.EducationRequest;
import com.uca.rrhhbackend.dto.response.EducationResponse;
import com.uca.rrhhbackend.entity.User;

import java.util.List;

public interface EducationService {
    EducationResponse create(User currentUser, EducationRequest request);

    List<EducationResponse> findMyEducation(User currentUser);

    EducationResponse update(
            User currentUser,
            Long educationId,
            EducationRequest request
    );

    void delete(User currentUser, Long educationId);
}