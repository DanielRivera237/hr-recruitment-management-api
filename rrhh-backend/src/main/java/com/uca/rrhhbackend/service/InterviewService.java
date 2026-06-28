package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.InterviewRequest;
import com.uca.rrhhbackend.dto.request.InterviewStatusRequest;
import com.uca.rrhhbackend.dto.response.InterviewResponse;

import java.util.List;

public interface InterviewService {

    InterviewResponse create(InterviewRequest request);

    List<InterviewResponse> findAll();

    InterviewResponse findById(Long id);

    List<InterviewResponse> findByApplication(Long applicationId);

    InterviewResponse update(Long id, InterviewRequest request);

    InterviewResponse updateStatus(
            Long id,
            InterviewStatusRequest request
    );
}