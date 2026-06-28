package com.uca.rrhhbackend.dto.response;

import java.time.LocalDateTime;

public record RecruiterCommentResponse(
        Long id,
        String comment,
        LocalDateTime createdAt,
        Long applicationId,
        Long recruiterProfileId
) {
}