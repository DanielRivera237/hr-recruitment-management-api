package com.uca.rrhhbackend.dto.response;

import java.time.LocalDate;

public record WorkExperienceResponse(
        Long id,
        String companyName,
        String position,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        Boolean currentlyWorking
) {
}