package com.uca.rrhhbackend.dto.request;

import com.uca.rrhhbackend.entity.enums.InterviewType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record InterviewRequest(

        @NotNull(message = "La fecha de entrevista es obligatoria")
        @Future(message = "La fecha de entrevista debe ser futura")
        LocalDateTime scheduledDate,

        @NotBlank(message = "El enlace de reunión es obligatorio")
        @Size(max = 500)
        String meetingUrl,

        @NotNull(message = "El tipo de entrevista es obligatorio")
        InterviewType type,

        @NotNull(message = "La postulación es obligatoria")
        Long applicationId,

        @NotNull(message = "El reclutador es obligatorio")
        Long recruiterProfileId
) {
}
