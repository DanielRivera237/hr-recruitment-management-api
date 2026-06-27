package com.uca.rrhhbackend.dto.request;

import com.uca.rrhhbackend.entity.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record ApplicationStatusRequest(

        @NotNull(message = "El estado es obligatorio")
        ApplicationStatus status
) {
}