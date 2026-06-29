package com.uca.rrhhbackend.dto.response;

import com.uca.rrhhbackend.entity.enums.InterviewStatus;
import com.uca.rrhhbackend.entity.enums.InterviewType;

import java.time.LocalDateTime;

public record InterviewResponse(
        Long id,
        LocalDateTime scheduledDate,
        String meetingUrl,
        InterviewType type,
        InterviewStatus status,
        Long applicationId,
        Long recruiterProfileId
) {
}