package com.uca.rrhhbackend.dto.response;

import com.uca.rrhhbackend.entity.enums.TechnicalTestStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TechnicalTestResponse(
        Long id,
        String title,
        String externalUrl,
        LocalDateTime deadline,
        TechnicalTestStatus status,
        BigDecimal result,
        Long applicationId
) {
}