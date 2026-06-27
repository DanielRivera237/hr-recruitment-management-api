package com.uca.rrhhbackend.dto.request;

import com.uca.rrhhbackend.entity.enums.JobModality;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record JobOfferRequest(

        @NotBlank(message = "El título es obligatorio")
        @Size(max = 150)
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 2000)
        String description,

        @NotBlank(message = "Los requisitos son obligatorios")
        @Size(max = 2000)
        String requirements,

        @NotNull(message = "El salario mínimo es obligatorio")
        @PositiveOrZero(message = "El salario mínimo no puede ser negativo")
        BigDecimal salaryMin,

        @NotNull(message = "El salario máximo es obligatorio")
        @Positive(message = "El salario máximo debe ser mayor que cero")
        BigDecimal salaryMax,

        @NotNull(message = "La modalidad es obligatoria")
        JobModality modality,

        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 150)
        String location,

        @NotNull(message = "La fecha de cierre es obligatoria")
        @Future(message = "La fecha de cierre debe ser futura")
        LocalDate closingDate,

        @NotNull(message = "La empresa es obligatoria")
        Long companyId,

        @NotNull(message = "El reclutador es obligatorio")
        Long recruiterProfileId,

        Set<Long> skillIds
) {
}