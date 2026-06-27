package com.uca.rrhhbackend.service.impl;

import com.uca.rrhhbackend.dto.request.CompanyRequest;
import com.uca.rrhhbackend.dto.response.CompanyResponse;
import com.uca.rrhhbackend.entity.Company;
import com.uca.rrhhbackend.exception.ConflictException;
import com.uca.rrhhbackend.exception.ResourceNotFoundException;
import com.uca.rrhhbackend.mapper.CompanyMapper;
import com.uca.rrhhbackend.repository.CompanyRepository;
import com.uca.rrhhbackend.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public CompanyResponse create(CompanyRequest request) {
        if (companyRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new ConflictException("Ya existe una empresa con ese nombre");
        }

        Company company = CompanyMapper.toEntity(request);
        company.setActive(true);

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> findAll() {
        return companyRepository.findAll()
                .stream()
                .map(CompanyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse findById(Long id) {
        return CompanyMapper.toResponse(findEntityById(id));
    }

    @Override
    public CompanyResponse update(Long id, CompanyRequest request) {
        Company company = findEntityById(id);

        if (!company.getName().equalsIgnoreCase(request.name().trim())
                && companyRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new ConflictException("Ya existe una empresa con ese nombre");
        }

        CompanyMapper.updateEntity(company, request);

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    public void deactivate(Long id) {
        Company company = findEntityById(id);
        company.setActive(false);
        companyRepository.save(company);
    }

    private Company findEntityById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la empresa con id " + id
                        )
                );
    }
}