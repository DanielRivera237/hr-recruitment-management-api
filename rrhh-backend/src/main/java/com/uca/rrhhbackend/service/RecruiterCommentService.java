package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.RecruiterCommentRequest;
import com.uca.rrhhbackend.dto.response.RecruiterCommentResponse;

import java.util.List;

public interface RecruiterCommentService {

    RecruiterCommentResponse create(RecruiterCommentRequest request);

    List<RecruiterCommentResponse> findByApplication(Long applicationId);

    void delete(Long id);
}
