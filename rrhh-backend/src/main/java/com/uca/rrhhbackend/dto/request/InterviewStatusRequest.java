package com.uca.rrhhbackend.dto.request;

import com.uca.rrhhbackend.entity.enums.InterviewStatus;
import jakarta.validation.constraints.NotNull;

public record InterviewStatusRequest(

        @NotNull(message = "El estado es obligatorio")
        InterviewStatus status
) {
}