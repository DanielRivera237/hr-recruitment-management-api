package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.TechnicalTestRequest;
import com.uca.rrhhbackend.dto.request.TechnicalTestResultRequest;
import com.uca.rrhhbackend.dto.response.TechnicalTestResponse;

import java.util.List;

public interface TechnicalTestService {

    TechnicalTestResponse create(TechnicalTestRequest request);

    TechnicalTestResponse findById(Long id);

    List<TechnicalTestResponse> findByApplication(Long applicationId);

    TechnicalTestResponse submit(Long id);

    TechnicalTestResponse review(
            Long id,
            TechnicalTestResultRequest request
    );
}