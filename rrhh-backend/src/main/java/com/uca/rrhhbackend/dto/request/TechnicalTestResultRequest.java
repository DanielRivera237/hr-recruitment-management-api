package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TechnicalTestResultRequest(

        @NotNull(message = "El resultado es obligatorio")
        @DecimalMin(value = "0.00", message = "El resultado mínimo es 0")
        @DecimalMax(value = "100.00", message = "El resultado máximo es 100")
        BigDecimal result
) {
}