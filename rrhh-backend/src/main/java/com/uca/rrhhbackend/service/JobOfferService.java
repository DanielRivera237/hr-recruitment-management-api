package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.JobOfferRequest;
import com.uca.rrhhbackend.dto.response.JobOfferResponse;

import java.util.List;

public interface JobOfferService {

    JobOfferResponse create(JobOfferRequest request);

    List<JobOfferResponse> findAll();

    JobOfferResponse findById(Long id);

    JobOfferResponse update(Long id, JobOfferRequest request);

    JobOfferResponse publish(Long id);

    JobOfferResponse close(Long id);

    void cancel(Long id);
}