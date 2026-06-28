package com.uca.rrhhbackend.dto.response;

import java.math.BigDecimal;

public record ScoringResponse(
        Long applicationId,
        BigDecimal score
) {
}