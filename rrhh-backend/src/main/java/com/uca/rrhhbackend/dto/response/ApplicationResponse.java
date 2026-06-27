package com.uca.rrhhbackend.dto.response;

import com.uca.rrhhbackend.entity.enums.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        LocalDateTime applicationDate,
        String coverLetter,
        ApplicationStatus status,
        BigDecimal score,
        Long candidateProfileId,
        String candidateName,
        Long jobOfferId,
        String jobOfferTitle
) {
}