package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.RecruiterProfileRequest;
import com.uca.rrhhbackend.dto.response.RecruiterProfileResponse;

import java.util.List;

public interface RecruiterProfileService {

    RecruiterProfileResponse create(
            RecruiterProfileRequest request
    );

    List<RecruiterProfileResponse> findAll();

    RecruiterProfileResponse findById(Long id);

    List<RecruiterProfileResponse> findByCompany(
            Long companyId
    );

    RecruiterProfileResponse update(
            Long id,
            RecruiterProfileRequest request
    );

    void deactivate(Long id);
}