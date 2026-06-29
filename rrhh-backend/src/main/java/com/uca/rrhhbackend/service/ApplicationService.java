package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.ApplicationRequest;
import com.uca.rrhhbackend.dto.request.ApplicationStatusRequest;
import com.uca.rrhhbackend.dto.response.ApplicationResponse;
import com.uca.rrhhbackend.entity.User;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse create(
            User currentUser,
            ApplicationRequest request
    );

    List<ApplicationResponse> findAll();

    ApplicationResponse findById(Long id);

    ApplicationResponse findMyApplicationById(
            User currentUser,
            Long id
    );

    List<ApplicationResponse> findMyApplications(
            User currentUser
    );

    List<ApplicationResponse> findByCandidate(
            Long candidateProfileId
    );

    List<ApplicationResponse> findByJobOffer(
            Long jobOfferId
    );

    ApplicationResponse updateStatus(
            Long id,
            ApplicationStatusRequest request
    );
}