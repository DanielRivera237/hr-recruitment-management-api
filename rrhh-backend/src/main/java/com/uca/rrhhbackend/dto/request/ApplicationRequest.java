package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplicationRequest(

        @NotNull(message = "El candidato es obligatorio")
        Long candidateProfileId,

        @NotNull(message = "La vacante es obligatoria")
        Long jobOfferId,

        @Size(max = 2000, message = "La carta no puede superar 2000 caracteres")
        String coverLetter
) {
}