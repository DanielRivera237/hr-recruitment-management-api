package com.uca.rrhhbackend.service;

import com.uca.rrhhbackend.dto.request.CompanyRequest;
import com.uca.rrhhbackend.dto.response.CompanyResponse;

import java.util.List;

public interface CompanyService {

    CompanyResponse create(CompanyRequest request);

    List<CompanyResponse> findAll();

    CompanyResponse findById(Long id);

    CompanyResponse update(Long id, CompanyRequest request);

    void deactivate(Long id);
}