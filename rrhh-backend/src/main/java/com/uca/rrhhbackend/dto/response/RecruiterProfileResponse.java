package com.uca.rrhhbackend.dto.response;

public record RecruiterProfileResponse(

        Long id,
        Long userId,
        String name,
        String surname,
        String email,
        String position,
        Long companyId,
        String companyName,
        Boolean active
) {
}