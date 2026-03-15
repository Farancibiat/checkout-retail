package com.retail.checkout.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Producto disponible con promociones asociadas")
public record ProductCatalogEntryDto(
        @Schema(description = "Código del producto", example = "p-001")
        String sku,

        @Schema(description = "Nombre del producto", example = "Producto Demo 001")
        String name,

        @Schema(description = "Precio unitario", example = "99.90")
        BigDecimal price,

        @Schema(description = "Promociones que aplican a este producto (puede estar vacío)")
        List<PromotionSummaryDto> promotions
) {}
