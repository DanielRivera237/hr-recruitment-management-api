package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CandidateProfileRequest(
        @Size(max = 25, message = "El teléfono no puede superar los 25 caracteres")
        String phone,

        @Size(max = 255, message = "La dirección no puede superar los 255 caracteres")
        String address,

        @Size(max = 1500, message = "El resumen profesional no puede superar los 1500 caracteres")
        String professionalSummary,

        @Min(value = 0, message = "Los años de experiencia no pueden ser negativos")
        Integer yearsExperience,

        @DecimalMin(value = "0.00", message = "La expectativa salarial no puede ser negativa")
        BigDecimal salaryExpectation,

        @Size(max = 100, message = "La disponibilidad no puede superar los 100 caracteres")
        String availability
) {
}