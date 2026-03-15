package com.retail.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Promoción aplicable al producto")
public record PromotionSummaryDto(
        @Schema(description = "Tipo de promoción", example = "PROMOTION")
        String type,

        @Schema(description = "Descripción de la promoción", example = "10% dcto. por 2+ unidades de p-010")
        String description
) {}
