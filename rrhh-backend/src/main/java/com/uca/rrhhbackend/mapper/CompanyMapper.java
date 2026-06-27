package com.uca.rrhhbackend.mapper;

import com.uca.rrhhbackend.dto.request.CompanyRequest;
import com.uca.rrhhbackend.dto.response.CompanyResponse;
import com.uca.rrhhbackend.entity.Company;

public final class CompanyMapper {

    private CompanyMapper() {
    }

    public static Company toEntity(CompanyRequest request) {
        Company company = new Company();

        company.setName(request.name().trim());
        company.setDescription(request.description());
        company.setLocation(request.location().trim());
        company.setSector(request.sector().trim());
        company.setWebsite(request.website());

        return company;
    }

    public static void updateEntity(Company company, CompanyRequest request) {
        company.setName(request.name().trim());
        company.setDescription(request.description());
        company.setLocation(request.location().trim());
        company.setSector(request.sector().trim());
        company.setWebsite(request.website());
    }

    public static CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getDescription(),
                company.getLocation(),
                company.getSector(),
                company.getWebsite(),
                company.getActive(),
                company.getCreatedAt()
        );
    }
}