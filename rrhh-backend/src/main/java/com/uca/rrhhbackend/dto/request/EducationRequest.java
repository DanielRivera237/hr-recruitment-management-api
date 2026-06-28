package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EducationRequest(
        @NotBlank(message = "Debe colocar una institucion")
        @Size(max = 150, message = "el nombre no debe superar los 150 caracteres")
        String institution,

        @NotBlank(message = "Debe colocar el titulo")
        @Size(max = 150, message = "El nombre del titulo no debe superar los 150 caracteres")
        String degree,

        @NotBlank(message = "Debe colocar el área de estudio")
        @Size(max = 150, message = "El área de estudio no debe superar los 150 caracteres")
        String fieldOfStudy,

        @NotNull(message = "Debe colocar la fecha de inicio")
        LocalDate startDate,

        LocalDate endDate,

        Boolean currentlyStudying
) {
}