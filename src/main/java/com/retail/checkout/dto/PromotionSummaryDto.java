package com.retail.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Promoción aplicable al producto (estructura alineada a catalog.json)")
public record PromotionSummaryDto(
        @Schema(description = "Tipo de promoción", example = "quantity_discount")
        String type,

        @Schema(description = "Cantidad mínima para aplicar", example = "2")
        int minQuantity,

        @Schema(description = "Porcentaje de descuento", example = "10")
        BigDecimal percent
) {}
