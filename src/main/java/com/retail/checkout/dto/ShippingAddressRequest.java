package com.retail.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dirección de envío")
public record ShippingAddressRequest(
        @NotBlank(message = "street es requerido")
        @Schema(description = "Calle y número", example = "Av. Falsa 123", requiredMode = Schema.RequiredMode.REQUIRED)
        String street,

        @NotBlank(message = "city es requerido")
        @Schema(description = "Ciudad", example = "Ciudad", requiredMode = Schema.RequiredMode.REQUIRED)
        String city,

        @NotBlank(message = "zoneId es requerido")
        @Schema(description = "Identificador de zona", example = "zone-1", requiredMode = Schema.RequiredMode.REQUIRED)
        String zoneId
) {}
