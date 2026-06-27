package com.uca.rrhhbackend.dto.response;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        String location,
        String sector,
        String website,
        Boolean active,
        LocalDateTime createdAt
) {
}