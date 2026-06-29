package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecruiterCommentRequest(

        @NotBlank(message = "El comentario es obligatorio")
        @Size(max = 2000, message = "El comentario no puede superar 2000 caracteres")
        String comment,

        @NotNull(message = "La postulación es obligatoria")
        Long applicationId,

        @NotNull(message = "El reclutador es obligatorio")
        Long recruiterProfileId
) {
}