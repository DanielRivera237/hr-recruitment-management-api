package com.uca.rrhhbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyRequest(

        @NotBlank(message = "El nombre de la empresa es obligatorio")
        @Size(max = 150)
        String name,

        @Size(max = 1000)
        String description,

        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 150)
        String location,

        @NotBlank(message = "El sector es obligatorio")
        @Size(max = 100)
        String sector,

        @Size(max = 255)
        String website
) {
}