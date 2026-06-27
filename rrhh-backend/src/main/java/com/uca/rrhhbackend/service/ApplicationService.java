package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.ApplicationRequest;
import com.uca.rrhhbackend.dto.request.ApplicationStatusRequest;
import com.uca.rrhhbackend.dto.response.ApplicationResponse;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse create(ApplicationRequest request);

    List<ApplicationResponse> findAll();

    ApplicationResponse findById(Long id);

    List<ApplicationResponse> findByCandidate(Long candidateProfileId);

    List<ApplicationResponse> findByJobOffer(Long jobOfferId);

    ApplicationResponse updateStatus(
            Long id,
            ApplicationStatusRequest request
    );
}