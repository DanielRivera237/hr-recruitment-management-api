package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record WorkExperienceRequest(
        @NotBlank(message = "Debe colocar nombre de la empresa")
        @Size(max = 150, message = "El nombre de la empresa no puede superar los 150 caracteres")
        String companyName,

        @NotBlank(message = "El cargo es obligatorio")
        @Size(max = 150, message = "El cargo no puede superar los 150 caracteres")
        String position,

        @Size(max = 1500, message = "La descripción debe tener maximo 1500 caracteres")
        String description,

        @NotNull(message = "La fecha  es obligatoria")
        LocalDate startDate,

        LocalDate endDate,

        Boolean currentlyWorking
) {
}