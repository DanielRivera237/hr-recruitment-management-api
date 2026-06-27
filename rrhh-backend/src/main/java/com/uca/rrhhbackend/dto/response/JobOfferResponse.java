package com.uca.rrhhbackend.dto.response;

import com.uca.rrhhbackend.entity.enums.JobModality;
import com.uca.rrhhbackend.entity.enums.JobOfferStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record JobOfferResponse(
        Long id,
        String title,
        String description,
        String requirements,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        JobModality modality,
        String location,
        JobOfferStatus status,
        LocalDate publicationDate,
        LocalDate closingDate,
        LocalDateTime createdAt,
        Long companyId,
        String companyName,
        Long recruiterProfileId,
        Set<String> requiredSkills
) {
}