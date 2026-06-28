package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SkillRequest(
        @NotBlank(message = "El la habilidad es obligatoria")
        @Size(max = 100)
        String name
) {
}